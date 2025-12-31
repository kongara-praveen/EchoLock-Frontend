package com.example.echolock.ui.screens

// Required Imports
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
fun OnBoardingTamperScreen(onContinue: () -> Unit, onSkip: () -> Unit) {

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

        Spacer(modifier = Modifier.height(135.dp))      // matched with Images screen

        Box(
            modifier = Modifier
                .size(255.dp)
                .background(AppColors.PrimaryLight.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_tamper),
                contentDescription = "tamper icon",
                modifier = Modifier.size(90.dp)           // very close to image icon scale
            )
        }

        Spacer(modifier = Modifier.height(55.dp))        // same title distance

        Text(
            "Tamper Detection",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = AppColors.TextPrimary,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Verify the integrity of your files and detect\n" +
                    "any unauthorized changes instantly.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(75.dp))        // indicator aligned visually

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ○ First (inactive)
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(AppColors.BorderLight, CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            // ○ Second (inactive)
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(AppColors.BorderLight, CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            // ● Third (active)
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(AppColors.PrimaryDark, CircleShape)
            )
        }


        Spacer(modifier = Modifier.height(58.dp))        // button location matched perfectly

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

        Text(
            "Skip",
            fontSize = 14.sp,
            color = AppColors.TextSecondary,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}
