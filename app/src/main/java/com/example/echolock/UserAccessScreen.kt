package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.theme.AppColors

@Composable
fun UserAccessScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {

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
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔥 Same alignment top spacing as onboarding screens
        Spacer(modifier = Modifier.height(135.dp))

        // Icon inside circle like onboarding
        Box(
            modifier = Modifier
                .size(255.dp)
                .background(AppColors.PrimaryLight.copy(alpha = 0.2f), CircleShape),
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
            color = AppColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            "The professional standard for steganography integrity verification.",
            fontSize = 15.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(75.dp))

        // 🔘 LOGIN BUTTON
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text("Login", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 🔗 CREATE ACCOUNT
        Text(
            "Create Account",
            color = AppColors.PrimaryDark,
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
            color = AppColors.TextTertiary
        )
    }
}
