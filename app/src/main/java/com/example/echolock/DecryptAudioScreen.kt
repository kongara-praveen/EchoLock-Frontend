package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun DecryptAudioScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        // Top Bar (Back)
        Text(
            text = "<  Decrypt Audio",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(25.dp))

        // File Upload Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(Color(0xFFE9F7F7), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Select Encrypted File",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF005F73)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Upload audio to extract message",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7E80)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Files Title
        Text(
            "Recent Encrypted Files",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Recent File Items
        DecryptAudioItem("secret_mission.mp3", "Encrypted • 2 days ago")
        Spacer(modifier = Modifier.height(12.dp))
        DecryptAudioItem("confidential_brief.wav", "Encrypted • 2 days ago")

        Spacer(modifier = Modifier.height(30.dp))

        // Continue Button
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Continue",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DecryptAudioItem(fileName: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(Color(0xFFF6F8F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_music),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(fileName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(date, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}
