package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
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
fun TermsConditionsScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {

        // ---------------- Header Row ---------------- //
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Terms & Conditions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF062A2F)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))


        // ---------------- TERMS CONTENT ---------------- //
        TermSection(
            title = "1. Acceptance of Terms",
            body = "By accessing and using EchoLock, you accept and agree to " +
                    "be bound by the terms and provisions of this agreement."
        )

        Spacer(modifier = Modifier.height(18.dp))

        TermSection(
            title = "2. Security & Privacy",
            body = "EchoLock is designed for secure communication. While we " +
                    "employ industry-standard encryption, you acknowledge that no " +
                    "digital transmission is 100% secure."
        )

        Spacer(modifier = Modifier.height(18.dp))

        TermSection(
            title = "3. User Responsibilities",
            body = "You agree not to use the application for any illegal purposes " +
                    "or to hide malicious code within media files."
        )

        Spacer(modifier = Modifier.height(18.dp))

        TermSection(
            title = "4. Intellectual Property",
            body = "The algorithms and design of EchoLock are proprietary and " +
                    "protected by copyright laws."
        )

        Spacer(modifier = Modifier.height(18.dp))

        TermSection(
            title = "5. Limitation of Liability",
            body = "EchoLock shall not be liable for any indirect, incidental, " +
                    "special, consequential or punitive damages."
        )
    }
}

@Composable
fun TermSection(title: String, body: String) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF062A2F))
    Spacer(Modifier.height(6.dp))
    Text(text = body, fontSize = 14.sp, color = Color(0xFF67777D), lineHeight = 19.sp)
}
