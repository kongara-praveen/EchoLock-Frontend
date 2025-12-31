package com.example.echolock.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.AudioSteganography
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun EncryptionProgressScreen(
    onCompleted: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }   // 0f → 1f
    var percent by remember { mutableIntStateOf(0) }     // 0 → 100
    var error by remember { mutableStateOf<String?>(null) }

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "content_scale"
    )

    LaunchedEffect(Unit) {
        try {
            val wavPath = UserSession.originalAudioPath
                ?: throw Exception("WAV audio missing")

            val message = UserSession.secretMessage
                ?: throw Exception("Secret message missing")
            
            val password = UserSession.encryptionPassword
                ?: throw Exception("Password missing")

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

            // Animate progress from 0 to 100% while encrypting
            for (i in 1..100) {
                delay(35)       // Adjust speed if needed
                percent = i
                progress = i / 100f
                
                // Start encryption at 20% progress
                if (i == 20) {
                    AudioSteganography.encode(
                        wavFile = wavFile,
                        message = message,
                        password = password,
                        outputFile = stegoFile
                    )
                    UserSession.stegoAudioPath = stegoFile.absolutePath
                }
            }

            delay(500)
            onCompleted()

        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Text(
                text = error ?: "Error occurred",
                fontSize = 16.sp,
                color = AppColors.Error,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        } else {
            CircularProgressIndicator(
                progress = progress,
                color = AppColors.PrimaryDark,
                strokeWidth = 8.dp,
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale),
                trackColor = AppColors.BorderLight
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "$percent%",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Encrypting your message...\nPlease wait.",
                fontSize = 16.sp,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
