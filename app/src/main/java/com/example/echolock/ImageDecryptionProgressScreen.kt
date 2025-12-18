package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageDecryptionProgressScreen(onCompleted: () -> Unit) {

    var progress by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (progress < 100) {
            kotlinx.coroutines.delay(40)
            progress++
        }
        onCompleted()
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            progress = progress / 100f,
            color = Color(0xFF005F73),
            strokeWidth = 6.dp,
            modifier = Modifier.size(110.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            "${progress}%",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Decrypting image...",
            fontSize = 15.sp,
            color = Color.Gray
        )
    }
}
