package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
fun LogoutConfirmationScreen(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
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
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Red Circle Icon
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(AppColors.Error.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout), // Use your logout icon
                contentDescription = null,
                tint = AppColors.Error,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            "Log Out?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Are you sure you want to log out? Your\nencrypted files will remain safe on this device.",
            fontSize = 15.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Confirm Button
        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Error),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text("Yes, Log Out", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Cancel
        Text(
            text = "Cancel",
            color = AppColors.PrimaryDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onCancel() }
        )
    }
}
