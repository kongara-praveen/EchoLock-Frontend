package com.example.echolock.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.theme.AppColors
import kotlinx.coroutines.delay
import com.example.echolock.ui.theme.GradientBackgrounds

@Composable
fun ImageEncryptionProgressScreen(
    onCompleted: () -> Unit
) {

    var progress by remember { mutableStateOf(0f) }   // 0f → 1f
    var percent by remember { mutableStateOf(0) }     // 0 → 100

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "content_scale"
    )

    // Animate progress from 0 to 100%
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(35)       // Adjust speed if needed
            percent = i
            progress = i / 100f
        }
        delay(500)
        onCompleted()       // Redirect after finishing
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            progress = progress,
            color = Color.White,
            strokeWidth = 8.dp,
            modifier = Modifier
                .size(140.dp)
                .scale(scale),
            trackColor = AppColors.BorderLight
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "$percent%",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Encrypting your image...\nPlease wait.",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}
