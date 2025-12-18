package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun DecryptImageResultScreen(onDone: () -> Unit) {

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                "Message Extracted",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Success Block
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = Color(0xFF00A86B),
                modifier = Modifier.size(40.dp)
            )
            Column(Modifier.padding(start = 10.dp)) {
                Text("Decryption Successful", fontWeight = FontWeight.Bold)
                Text("Source: classified_doc.png", fontSize = 13.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Message Box
        Box(
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xFFF7F9FA), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                "OPERATION NIGHTFALL – PHASE 2\n\n" +
                        "Asset extraction confirmed for 0300 hours local time. Rendezvous point: Alpha-7.\n" +
                        "Authentication code: ECHO-LOCK-7749\n\n" +
                        "Destroy after reading.",
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        // Copy button
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7FBF6)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_copy),
                contentDescription = null,
                tint = Color(0xFF005F73)
            )
            Spacer(Modifier.width(6.dp))
            Text("Copy to Clipboard", color = Color(0xFF005F73))
        }

        Spacer(Modifier.height(20.dp))

        // Done button
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done", color = Color.White)
        }
    }
}
