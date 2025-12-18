package com.example.echolock.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FAQScreen(onBack: () -> Unit) {

    val faqItems = listOf(
        "Is my data stored on servers?" to
                "No. EchoLock uses a zero-knowledge architecture. All encryption and storage happens locally on your device.",

        "What file formats are supported?" to
                "We support MP3, WAV, FLAC for audio, and PNG, JPG, WEBP for images.",

        "Can I recover a forgotten password?" to
                "Yes, but only if you have set up recovery keys or have access to your verified email address.",

        "How secure is the encryption?" to
                "We use military-grade AES-256 encryption combined with advanced steganography algorithms."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(26.dp).clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text("FAQ", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))

        faqItems.forEach {
            FAQItem(question = it.first, answer = it.second)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                question,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        if (expanded) {
            Spacer(Modifier.height(8.dp))
            Text(answer, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
