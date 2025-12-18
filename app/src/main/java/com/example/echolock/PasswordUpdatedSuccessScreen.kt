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
fun PasswordUpdatedSuccessScreen(
    onFinished: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(1800)   // Auto redirect in 1.8 sec
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFE6FFF1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF06C167),
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Password Updated \n Successfully",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))


    }
}
