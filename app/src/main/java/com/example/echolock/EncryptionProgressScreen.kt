package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import kotlinx.coroutines.delay

@Composable
fun EncryptionProgressScreen(
    onCompleted: () -> Unit
) {

    var progress by remember { mutableStateOf(0) }   // 0 → 100

    // Animate progress
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            progress = i
            delay(20)   // speed (20ms × 100 = 2 seconds)
        }
        onCompleted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Icon
        Image(
            painter = painterResource(id = R.drawable.ic_lock),
            contentDescription = "Encrypting",
            modifier = Modifier.size(90.dp)
        )

        Spacer(Modifier.height(30.dp))

        // Circular indicator with animation
        CircularProgressIndicator(
            progress = progress / 100f,     // Convert to 0f–1f
            color = Color(0xFF005F73),
            strokeWidth = 5.dp,
            modifier = Modifier.size(70.dp)
        )

        Spacer(Modifier.height(20.dp))

        // 🔥 Percentage Text
        Text(
            text = "$progress%",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF005F73),
        )

        Spacer(Modifier.height(25.dp))

        // Title
        Text(
            text = "Encrypting your message...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Please wait while we securely hide your data inside the audio file.",
            fontSize = 14.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
    }
}
