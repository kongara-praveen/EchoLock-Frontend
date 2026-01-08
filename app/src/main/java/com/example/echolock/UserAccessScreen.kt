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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.theme.GradientBackgrounds

@Composable
fun UserAccessScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(135.dp))

        /* 🔵 APP ICON / LOGO */
        Box(
            modifier = Modifier.size(260.dp),
            contentAlignment = Alignment.Center
        ) {

            // Soft glow
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF38BDF8).copy(alpha = 0.45f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Solid inner circle
            Box(
                modifier = Modifier
                    .size(165.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tamper), // or app logo
                    contentDescription = "App Logo",
                    modifier = Modifier.size(96.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        Color(0xFF0F172A)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(55.dp))

        /* 🔥 TITLE */
        Text(
            text = "Welcome to EchoLock",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEAF6FF),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        /* 📝 SUBTITLE */
        Text(
            text = "The professional standard for secure audio and image steganography.",
            fontSize = 16.sp,
            color = Color(0xFFBEE7E8),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(75.dp))

        /* ▶ LOGIN BUTTON */
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF38BDF8)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        /* 🔗 CREATE ACCOUNT */
        Text(
            text = "Create Account",
            color = Color(0xFF38BDF8),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onCreateAccountClick() }
        )

        Spacer(modifier = Modifier.height(48.dp))

        /* 📝 FOOTER NOTE */
        Text(
            text = "By continuing, you agree to our Terms of Service and\nPrivacy Policy.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF94A3B8)
        )
    }
}
