package com.example.echolock.util

import android.content.Context
import android.media.*
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object AudioConverter {

    fun convertToWav(
        context: Context,
        inputUri: Uri
    ): File {
        val result = convertToWavWithProgress(context, inputUri, null)
        return result.file
    }

    fun convertToWavWithProgress(
        context: Context,
        inputUri: Uri,
        progressFlow: MutableStateFlow<Float>?
    ): ConversionResult {

        val extractor = MediaExtractor()
        extractor.setDataSource(context, inputUri, null)

        var trackIndex = -1
        for (i in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (mime?.startsWith("audio/") == true) {
                trackIndex = i
                break
            }
        }
        if (trackIndex == -1) {
            throw Exception("No audio track found")
        }

        extractor.selectTrack(trackIndex)
        val format = extractor.getTrackFormat(trackIndex)
        val mime = format.getString(MediaFormat.KEY_MIME)!!

        val sampleRate =
            format.getInteger(MediaFormat.KEY_SAMPLE_RATE)

        val channels =
            format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

        val pcmEncoding = AudioFormat.ENCODING_PCM_16BIT

        val codec = MediaCodec.createDecoderByType(mime)
        codec.configure(format, null, null, 0)
        codec.start()

        val outputFile = File(
            context.cacheDir,
            "converted_${System.currentTimeMillis()}.wav"
        )

        val fos = FileOutputStream(outputFile)

        // Placeholder WAV header
        fos.write(ByteArray(44))

        // Get duration for progress calculation
        val durationUs = format.getLong(MediaFormat.KEY_DURATION)
        val durationMs = if (durationUs > 0) durationUs / 1000 else 0L

        val bufferInfo = MediaCodec.BufferInfo()
        var totalAudioLen = 0
        var isEOS = false
        var lastProgressUpdate = 0f

        while (true) {

            if (!isEOS) {
                val inIndex = codec.dequeueInputBuffer(10_000)
                if (inIndex >= 0) {
                    val inputBuffer = codec.getInputBuffer(inIndex)!!
                    val size = extractor.readSampleData(inputBuffer, 0)

                    if (size < 0) {
                        codec.queueInputBuffer(
                            inIndex, 0, 0, 0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                        isEOS = true
                    } else {
                        codec.queueInputBuffer(
                            inIndex, 0, size,
                            extractor.sampleTime, 0
                        )
                        extractor.advance()
                    }
                }
            }

            val outIndex = codec.dequeueOutputBuffer(bufferInfo, 10_000)
            if (outIndex >= 0) {
                val outBuffer = codec.getOutputBuffer(outIndex)!!
                val pcmBytes = ByteArray(bufferInfo.size)
                outBuffer.get(pcmBytes)
                outBuffer.clear()

                fos.write(pcmBytes)
                totalAudioLen += pcmBytes.size

                // Update progress
                if (durationMs > 0 && progressFlow != null) {
                    val currentTime = bufferInfo.presentationTimeUs / 1000
                    val progress = (currentTime.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                    
                    // Update only if progress changed significantly (every 1%)
                    if (progress - lastProgressUpdate >= 0.01f || progress >= 0.99f) {
                        progressFlow.value = progress
                        lastProgressUpdate = progress
                    }
                }

                codec.releaseOutputBuffer(outIndex, false)
            }

            if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                break
            }
        }

        codec.stop()
        codec.release()
        extractor.release()
        fos.close()

        // 🔹 Rewrite correct WAV header
        writeWavHeader(
            outputFile,
            totalAudioLen,
            sampleRate,
            channels
        )

        // Final progress update
        progressFlow?.value = 1f

        return ConversionResult(outputFile, totalAudioLen, sampleRate, channels)
    }

    data class ConversionResult(
        val file: File,
        val audioLength: Int,
        val sampleRate: Int,
        val channels: Int
    )

    private fun writeWavHeader(
        wavFile: File,
        audioLen: Int,
        sampleRate: Int,
        channels: Int
    ) {
        val byteRate = sampleRate * channels * 2
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)

        header.put("RIFF".toByteArray())
        header.putInt(audioLen + 36)
        header.put("WAVE".toByteArray())
        header.put("fmt ".toByteArray())
        header.putInt(16)
        header.putShort(1) // PCM
        header.putShort(channels.toShort())
        header.putInt(sampleRate)
        header.putInt(byteRate)
        header.putShort((channels * 2).toShort())
        header.putShort(16)
        header.put("data".toByteArray())
        header.putInt(audioLen)

        val raf = wavFile.outputStream()
        raf.write(header.array())
        raf.close()
    }
}
