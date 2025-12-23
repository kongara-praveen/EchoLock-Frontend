package com.example.echolock.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.util.AudioConverter
import com.example.echolock.util.AudioSteganography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
@Composable
fun EncryptionProgressScreen(
    onCompleted: () -> Unit
) {
    var progress by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val wavPath = UserSession.originalAudioPath
                ?: throw Exception("WAV audio missing")

            val message = UserSession.secretMessage
                ?: throw Exception("Secret message missing")

            val wavFile = File(wavPath)

            // Capacity check
            val maxChars = AudioSteganography.getMaxMessageLength(wavFile)
            if (message.length > maxChars) {
                throw Exception("Message too large. Max allowed: $maxChars characters")
            }

            val stegoFile = File(
                wavFile.parent,
                "stego_${System.currentTimeMillis()}.wav"
            )

            AudioSteganography.encode(
                wavFile = wavFile,
                message = message,
                outputFile = stegoFile
            )

            UserSession.stegoAudioPath = stegoFile.absolutePath

            for (i in 1..100) {
                progress = i
                delay(10)
            }

            onCompleted()

        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(progress = progress / 100f)
        Spacer(Modifier.height(12.dp))
        Text(text = error ?: "Encrypting your message…")
    }
}
