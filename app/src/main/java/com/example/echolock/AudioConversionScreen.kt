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
import androidx.compose.ui.unit.dp
import com.example.echolock.session.UserSession
import com.example.echolock.util.AudioConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun AudioConversionScreen(
    onSuccess: () -> Unit,
    onFailed: () -> Unit
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            /* 1️⃣ VALIDATION */
            val originalPath = UserSession.originalAudioPath
                ?: throw Exception("Original audio path missing")

            /* 2️⃣ CONVERT TO WAV (BACKGROUND THREAD) */
            val wavFile = withContext(Dispatchers.IO) {
                AudioConverter.convertToWav(
                    context = context,
                    inputUri = Uri.fromFile(File(originalPath))
                )
            }

            /* 3️⃣ SAVE WAV PATH */
            UserSession.wavAudioPath = wavFile.absolutePath

            /* 4️⃣ NAVIGATE */
            onSuccess()

        } catch (e: Exception) {
            error = e.message ?: "Audio conversion failed"
            onFailed()
        }
    }

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            color = Color(0xFF005F73),
            strokeWidth = 5.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = error ?: "Converting audio to WAV…",
            color = if (error != null) Color.Red else Color.Gray
        )
    }
}
