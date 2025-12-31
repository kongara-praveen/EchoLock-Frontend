package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.theme.AppColors

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {

    // Screen entrance animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
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
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Privacy Policy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
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
    Text(
        title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.TextPrimary
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text,
        fontSize = 14.sp,
        color = AppColors.TextSecondary,
        lineHeight = 20.sp
    )
    Spacer(modifier = Modifier.height(18.dp))
}
