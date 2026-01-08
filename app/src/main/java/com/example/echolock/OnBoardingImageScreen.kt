package com.example.echolock.ui.screens

// 🔥 Required Imports (DON'T MISS)
import androidx.compose.ui.graphics.Brush
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
import com.example.echolock.ui.theme.GradientBackgrounds

@Composable
fun OnBoardingImageScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit
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

        /* 🖼️ IMAGE ICON – HIGH VISIBILITY (MATCHES AUDIO SCREEN) */
        Box(
            modifier = Modifier.size(260.dp),
            contentAlignment = Alignment.Center
        ) {

            // 🌈 Outer glow
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

            // ⚪ Solid inner circle (contrast layer)
            Box(
                modifier = Modifier
                    .size(165.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {

                // 🖼️ IMAGE ICON (DARK FOR CONTRAST)
                Image(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "Image Icon",
                    modifier = Modifier.size(100.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        Color(0xFF0F172A) // dark slate
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(52.dp))

        /* 🔥 TITLE */
        Text(
            text = "Hide Messages in Images",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(14.dp))

        /* 📝 DESCRIPTION */
        Text(
            text = "Securely embed secret data inside image pixels using LSB steganography",
            fontSize = 16.sp,
            color = Color(0xFFBEE7E8),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(72.dp))

        /* 🔘 PAGE INDICATORS */
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(Color.White.copy(alpha = 0.35f), CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF38BDF8), CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(Color.White.copy(alpha = 0.35f), CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(58.dp))

        /* ▶ CONTINUE BUTTON */
        Button(
            onClick = onContinue,
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
                text = "Continue",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        /* ⏭ SKIP */
        Text(
            text = "Skip",
            color = Color(0xFFBEE7E8),
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}
