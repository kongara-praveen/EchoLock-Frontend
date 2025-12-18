package com.example.echolock.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageEncryptionCompleteScreen(
    onDownload: () -> Unit,
    onBackDashboard: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00A86B),
            modifier = Modifier.size(110.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Encryption Complete",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your message has been securely hidden within\nthe image file.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8F9)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(Modifier.padding(20.dp)) {

                Text(
                    "Security Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF062A2F)
                )

                Spacer(modifier = Modifier.height(15.dp))

                ImageReportRow(label = "Stego Method", value = "LSB - Image")
                Spacer(modifier = Modifier.height(10.dp))
                ImageReportRow(label = "File Size", value = "4.1 MB")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onDownload,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Download Image", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Back to Dashboard",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF005F73),
            modifier = Modifier.clickable { onBackDashboard() }
        )
    }
}

@Composable
fun ImageReportRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF6B7E80))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
