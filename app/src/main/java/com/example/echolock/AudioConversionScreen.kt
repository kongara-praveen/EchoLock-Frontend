package com.example.echolock.ui.screens

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.AudioConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun AudioConversionScreen(
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }
    val progressFlow = remember { MutableStateFlow(0f) }
    val progress by progressFlow.collectAsState()

    // Screen entrance animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    LaunchedEffect(Unit) {
        try {
            /* 1️⃣ VALIDATION */
            val originalPath = UserSession.originalAudioPath
                ?: throw Exception("Original audio path missing")

            /* 2️⃣ CONVERT TO WAV WITH PROGRESS (BACKGROUND THREAD) */
            val result = withContext(Dispatchers.IO) {
                AudioConverter.convertToWavWithProgress(
                    context = context,
                    inputUri = Uri.fromFile(File(originalPath)),
                    progressFlow = progressFlow
                )
            }

            /* 3️⃣ SAVE WAV PATH */
            UserSession.wavAudioPath = result.file.absolutePath

            /* 4️⃣ NAVIGATE */
            onSuccess()

        } catch (e: Exception) {
            error = e.message ?: "Audio conversion failed"
            onFailed()
        }
    }

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Progress indicator
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(120.dp),
                color = AppColors.PrimaryDark,
                strokeWidth = 8.dp,
                trackColor = AppColors.BorderLight
            )
            
            // Percentage text
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = error ?: "Converting audio to WAV…",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (error != null) AppColors.Error else AppColors.TextSecondary
        )

        if (error == null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please wait while we process your audio file",
                fontSize = 14.sp,
                color = AppColors.TextTertiary
            )
        }
    }
}
