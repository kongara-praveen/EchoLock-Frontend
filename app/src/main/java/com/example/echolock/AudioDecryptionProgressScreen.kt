package com.example.echolock.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.util.AudioSteganography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun AudioDecryptionProgressScreen(
    onCompleted: () -> Unit
) {
    val context = LocalContext.current
    var progress by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {

        // 🔐 REAL DECRYPTION
        withContext(Dispatchers.IO) {
            try {
                val uriString = UserSession.decryptAudioUri
                    ?: throw Exception("Encrypted audio missing")

                val uri = Uri.parse(uriString)

                // ✅ Convert content:// URI → local file
                val inputStream =
                    context.contentResolver.openInputStream(uri)
                        ?: throw Exception("Unable to read encrypted audio")

                val audioFile = File(
                    context.cacheDir,
                    "decrypt_${System.currentTimeMillis()}.wav"
                )

                FileOutputStream(audioFile).use { output ->
                    inputStream.copyTo(output)
                }

                // 🔓 Decode message
                val extractedMessage =
                    AudioSteganography.decode(audioFile)

                UserSession.decryptedMessage =
                    if (extractedMessage.isBlank())
                        "No hidden message found"
                    else
                        extractedMessage

            } catch (e: Exception) {
                UserSession.decryptedMessage = "Decryption failed"
            }
        }

        // 🎯 Progress animation (UI only)
        while (progress < 100) {
            delay(25)
            progress++
        }

        onCompleted()
    }

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            progress = progress / 100f,
            color = Color(0xFF005F73),
            strokeWidth = 6.dp,
            modifier = Modifier.size(110.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "$progress%",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Text(
            text = "Decrypting audio...",
            fontSize = 15.sp,
            color = Color.Gray
        )
    }
}
