package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable   // ⭐ FIX — missing import
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
fun PrivacyPolicyScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // BACK BUTTON
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onBack() }     // ← Now recognized
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Privacy Policy", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // ------------------- POLICY CONTENT ------------------- //
        PolicyBlock(
            title = "1. Data Collection",
            text = "EchoLock is designed with privacy first. We do not store your messages or media files on our servers."
        )

        PolicyBlock(
            title = "2. Local Storage",
            text = "Encrypted files remain within your device's secure storage. You can delete them anytime."
        )

        PolicyBlock(
            title = "3. Analytics",
            text = "Minimal performance analytics may be collected to improve app stability. This can be disabled."
        )

        PolicyBlock(
            title = "4. Third Parties",
            text = "We do not share your data with government bodies, advertisers, or external services unless required by law."
        )
    }
}

@Composable
fun PolicyBlock(title: String, text: String) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    Spacer(modifier = Modifier.height(6.dp))
    Text(text, fontSize = 14.sp, color = Color.DarkGray)
    Spacer(modifier = Modifier.height(18.dp))
}
