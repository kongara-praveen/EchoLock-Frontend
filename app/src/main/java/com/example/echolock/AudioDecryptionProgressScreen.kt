package com.example.echolock.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
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

                // 🔓 Decode message with password
                val password = UserSession.decryptionPassword ?: ""
                val result = AudioSteganography.decode(audioFile, password)

                UserSession.decryptedMessage = result.message

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

    // Animation for progress
    val progressAlpha by animateFloatAsState(
        targetValue = if (progress > 0) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "progress_alpha"
    )

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            progress = progress / 100f,
            color = AppColors.PrimaryDark,
            strokeWidth = 6.dp,
            modifier = Modifier
                .size(110.dp)
                .alpha(progressAlpha)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "$progress%",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Decrypting audio...",
            fontSize = 16.sp,
            color = AppColors.TextSecondary
        )
    }
}
