package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun LogoutConfirmationScreen(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Red Circle Icon
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFFFFE8E8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout), // Use your logout icon
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            "Log Out?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Are you sure you want to log out? Your\nencrypted files will remain safe on this device.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Confirm Button
        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Yes, Log Out", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Cancel
        Text(
            text = "Cancel",
            color = Color(0xFF005F73),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onCancel() }
        )
    }
}
