package com.example.echolock.ui.screens

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun WelcomeScreen(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2400)
        onFinished()
    }

    val fade = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        fade.animateTo(1f, tween(800))
    }
    val titleScale by rememberInfiniteTransition(label = "titleScale")
        .animateFloat(
            initialValue = 0.98f,
            targetValue = 1.02f,
            animationSpec = infiniteRepeatable(
                tween(3000, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            ),
            label = "titleScale"
        )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0B132B),
                        Color(0xFF1C2541),
                        Color(0xFF3A506B)
                    )
                )
            )
            .alpha(fade.value),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            AudioSteganographyAnimation()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "EchoLock",
                modifier = Modifier.scale(titleScale),
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.6.sp
            )


            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "A Audio Steganography",
                fontSize = 18.sp,              // 🔥 More readable
                color = Color(0xFFBEE7E8),
                textAlign = TextAlign.Center,
                letterSpacing = 0.6.sp
            )

        }
    }
}
@Composable
fun AudioSteganographyAnimation() {

    val wavePhase by rememberInfiniteTransition(label = "wave")
        .animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(2600, easing = LinearEasing)
            ),
            label = "wavePhase"
        )

    val scale by rememberInfiniteTransition(label = "scale")
        .animateFloat(
            0.95f, 1.05f,
            animationSpec = infiniteRepeatable(
                tween(3000, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            ),
            label = "scale"
        )

    Box(
        modifier = Modifier
            .size(260.dp)   // 🔥 BIG CENTER ANIMATION
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val midY = size.height / 2
            val step = size.width / 40

            // 🎧 Audio waveform
            for (i in 0..40) {
                val x = i * step
                val amplitude = sin(wavePhase + i * 0.4f) * 40

                drawLine(
                    color = Color(0xFF5EEAD4),
                    start = Offset(x, midY - amplitude),
                    end = Offset(x, midY + amplitude),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }

            // 🔹 Hidden data dots (steganography)
            repeat(12) { i ->
                drawCircle(
                    color = Color(0xFF38BDF8),
                    radius = 4f,
                    center = Offset(
                        x = (i * 20f + 40f) % size.width,
                        y = midY + sin(wavePhase + i) * 25
                    )
                )
            }
        }
    }
}
