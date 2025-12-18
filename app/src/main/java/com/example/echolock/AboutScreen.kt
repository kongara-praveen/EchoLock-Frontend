package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun AboutScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(26.dp).clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("About", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_shield),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "EchoLock",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            "Version 1.0.4 (Build 2024)",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text("Mission", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "EchoLock provides professional-grade steganography tools for investigative journalists, cybersecurity teams, and intelligence professionals to communicate securely.",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Core Features", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))

        FeatureBullet("AES-256 Encryption")
        FeatureBullet("LSB Steganography (Audio & Image)")
        FeatureBullet("Tamper Detection & Integrity Checks")
        FeatureBullet("Secure Local Storage")
        FeatureBullet("Zero-Knowledge Architecture")

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            "© 2025 EchoLock Security Inc.\nAll rights reserved.",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FeatureBullet(text: String) {
    Row {
        Text("•  ", fontSize = 18.sp)
        Text(text, fontSize = 14.sp)
    }
}
