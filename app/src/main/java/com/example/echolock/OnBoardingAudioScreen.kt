package com.example.echolock.ui.screens

// 🔥 REQUIRED IMPORTS
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
fun OnBoardingAudioScreen(onContinue: () -> Unit, onSkip: () -> Unit) {

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

        Spacer(modifier = Modifier.height(135.dp))   // perfectly aligned like tamper screen

        Box(
            modifier = Modifier
                .size(255.dp)
                .background(AppColors.PrimaryLight.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mic),
                contentDescription = "mic icon",
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Text("Hide Messages in Audio",
            fontSize = 22.sp, fontWeight = FontWeight.Black, color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Securely hide messages within any audio file format",
            fontSize = 15.sp, color = AppColors.TextSecondary, textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(75.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            // ● Active dot (first)
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(AppColors.PrimaryDark, CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            // ○ Inactive dots (remaining 2)
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(AppColors.BorderLight, CircleShape)
                )
                Spacer(Modifier.width(10.dp))
            }
        }


        Spacer(modifier = Modifier.height(58.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) { Text("Continue", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold) }

        Spacer(modifier = Modifier.height(18.dp))

        Text("Skip",
            color = AppColors.TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}
