package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
fun UserAccessScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔥 Same alignment top spacing as onboarding screens
        Spacer(modifier = Modifier.height(135.dp))

        // Icon inside circle like onboarding
        Box(
            modifier = Modifier
                .size(255.dp)
                .background(Color(0xFFEFFBFB), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_tamper), // or your logo
                contentDescription = "logo",
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Text(
            "Welcome to EchoLock",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0A2E45),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            "The professional standard for steganography and media\n" +
                    "integrity verification.",
            fontSize = 15.sp,
            color = Color(0xFF5F7076),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(75.dp))

        // 🔘 LOGIN BUTTON
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login", color = Color.White, fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 🔗 CREATE ACCOUNT
        Text(
            "Create Account",
            color = Color(0xFF005F73),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onCreateAccountClick() }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 📝 Privacy Note
        Text(
            "By continuing, you agree to our Terms of Service and\nPrivacy Policy.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF8D9B9E)
        )
    }
}
