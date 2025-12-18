package com.example.echolock.ui.screens

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
fun DecryptImageScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
            Text(
                "Decrypt Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Upload Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFFEFFAF7), RoundedCornerShape(16.dp))
                .clickable { }
                .padding(20.dp)
        ) {

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    tint = Color(0xFF005F73),
                    modifier = Modifier.size(42.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text("Select Encrypted Image", fontWeight = FontWeight.Bold, color = Color(0xFF005F73))
                Text("Upload image to extract message", fontSize = 13.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(20.dp))

        Text("Recent Encrypted Images", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            RecentImageItem("classified_doc.png")
            RecentImageItem("intel_map.jpg")
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73))
        ) {
            Text("Continue", color = Color.White)
        }
    }
}

@Composable
fun RecentImageItem(name: String) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .background(Color(0xFFF3FAF7), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Icon(
            painterResource(R.drawable.ic_image_placeholder),
            contentDescription = null,
            tint = Color(0xFF005F73),
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Text("Encrypted", fontSize = 12.sp, color = Color.Gray)
    }
}
