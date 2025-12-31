package com.example.echolock.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.ui.theme.AppColors

@Composable
fun DashboardScreen(
    onEncryptAudio: () -> Unit,
    onEncryptImage: () -> Unit,
    onTamperCheck: () -> Unit,
    onDecryptAudio: () -> Unit,
    onDecryptImage: () -> Unit,

    onFilesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(0) }   // Home selected

    // Animation for screen entrance
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
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "What would you like to do today?",
                color = AppColors.TextSecondary,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            DashboardTile(
                icon = R.drawable.ic_mic,
                title = "Hide in Audio",
                subtitle = "Embed secret messages in audio files",
                bg = Color(0xFFDDF7FF),
                onClick = onEncryptAudio
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_image,
                title = "Hide in Image",
                subtitle = "Conceal data within image files",
                bg = Color(0xFFEDE4FF),
                onClick = onEncryptImage
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_tamper,
                title = "Tamper Check",
                subtitle = "Verify file integrity",
                bg = Color(0xFFE5FFF1),
                onClick = onTamperCheck
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                QuickBox(
                    text = "Decrypt Audio",
                    icon = R.drawable.ic_mic,
                    onClick = onDecryptAudio,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(12.dp))

                QuickBox(
                    text = "Decrypt Image",
                    icon = R.drawable.ic_image,
                    onClick = onDecryptImage,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Bottom Navigation Bar
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    1 -> onFilesClick()
                    2 -> onHistoryClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }
}

/*───────────────────────────────────────────────────────────────*/
/* Dashboard Tiles */
/*───────────────────────────────────────────────────────────────*/

@Composable
fun DashboardTile(icon: Int, title: String, subtitle: String, bg: Color, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tile_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = {
                isPressed = true
                onClick()
            }),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 1.dp)
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(bg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = AppColors.TextSecondary,
                    lineHeight = 18.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(AppColors.TextSecondary)
            )
        }
    }
}

/*───────────────────────────────────────────────────────────────*/
/* Quick Action Box */
/*───────────────────────────────────────────────────────────────*/

@Composable
fun QuickBox(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Reset pressed state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "quickbox_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(onClick = {
                isPressed = true
                onClick()
            }),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardElevated),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(AppColors.PrimaryLight.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
