package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EncryptionCompleteScreen(
    onDownload: () -> Unit,
    onBackDashboard: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ✔ Success Tick Icon (No drawable needed)
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFFE8FFF1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Success",
                tint = Color(0xFF1EC971),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Title
        Text(
            "Encryption Complete",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Your message has been securely hidden within the audio file.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 📄 Simple Security Report (no icons)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color(0xFFF6F8F9)),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    "Security Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF062A2F)
                )

                Spacer(modifier = Modifier.height(18.dp))

                ReportItem("Stego Method", "LSB-Audio")
                Spacer(modifier = Modifier.height(12.dp))

                ReportItem("File Size", "2.4 MB")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Download Button
        Button(
            onClick = onDownload,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Download File", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Back to Dashboard
        Text(
            text = "Back to Dashboard",
            color = Color(0xFF005F73),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onBackDashboard() }
        )
    }
}

@Composable
fun ReportItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF6B7E80), fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF062A2F), fontSize = 14.sp)
    }
}
