package com.example.echolock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ImageEncryptionProgressScreen(
    onCompleted: () -> Unit
) {

    var progress by remember { mutableStateOf(0f) }   // 0f → 1f
    var percent by remember { mutableStateOf(0) }     // 0 → 100

    // Animate progress from 0 to 100%
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(35)       // Adjust speed if needed
            percent = i
            progress = i / 100f
        }
        delay(500)
        onCompleted()       // Redirect after finishing
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            progress = progress,
            color = Color(0xFF005F73),
            strokeWidth = 6.dp,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "$percent%",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Encrypting your image...\nPlease wait.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center
        )
    }
}
