package com.example.echolock.ui.screens

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
import kotlinx.coroutines.delay

@Composable
fun AudioDecryptionProgressScreen(onCompleted: () -> Unit) {

    var progress by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (progress < 100) {
            delay(40)
            progress++
        }
        onCompleted()
    }

    Column(
        Modifier.fillMaxSize(),
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
