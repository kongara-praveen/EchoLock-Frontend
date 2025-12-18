package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
fun LogoutSuccessScreen(
    onFinished: () -> Unit
) {

    // Auto redirect after delay
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Green success icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFE7FFF1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = Color(0xFF1EC971),
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Logged Out Successfully",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Redirecting...",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
