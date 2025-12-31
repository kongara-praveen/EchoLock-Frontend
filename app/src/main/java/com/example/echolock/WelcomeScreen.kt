package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onFinished: () -> Unit
) {
    /* ---------------- TIMING ---------------- */
    val splashDuration = 2000L
    val fadeOutDuration = 400L

    /* ---------------- NAVIGATION ---------------- */
    LaunchedEffect(Unit) {
        delay(splashDuration)
        onFinished()
    }

    /* ---------------- WAVE MOTION ---------------- */
    val waveTransition = rememberInfiniteTransition(label = "wave")
    val waveShift by waveTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveShift"
    )

    /* ---------------- LOGO PULSE ---------------- */
    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    /* ---------------- FADE IN ---------------- */
    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alphaAnim.value),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(pulseScale)
        ) {

            /* ---------------- WAVE LOGO ---------------- */
            Canvas(modifier = Modifier.size(120.dp)) {
                val w = size.width
                val h = size.height
                val color = AppColors.PrimaryDark

                fun wave(y: Float, offset: Float) {
                    val path = Path().apply {
                        moveTo(0f, y)
                        cubicTo(
                            w * 0.25f, y - 14 + offset,
                            w * 0.75f, y + 14 - offset,
                            w, y
                        )
                    }
                    drawPath(path, color)
                }

                wave(h * 0.35f, waveShift)
                wave(h * 0.50f, -waveShift)
                wave(h * 0.65f, waveShift)
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* ---------------- TITLE ---------------- */
            Text(
                text = "EchoLock",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            /* ---------------- SUBTITLE ---------------- */
            Text(
                text = "A Audio Steganography",
                fontSize = 15.sp,
                color = AppColors.TextSecondary,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
