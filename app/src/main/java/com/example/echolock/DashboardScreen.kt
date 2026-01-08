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
import androidx.compose.ui.graphics.Brush
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
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B132B),
                        Color(0xFF1C2541),
                        Color(0xFF3A506B)
                    )
                )
            )
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
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "What would you like to do today?",
                color = Color(0xFFBEE7E8),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            DashboardTile(
                icon = R.drawable.ic_mic,
                title = "Hide in Audio",
                subtitle = "Embed secret messages in audio files",
                gradientColors = listOf(
                    Color(0xFF6366F1),
                    Color(0xFF4F46E5),
                    Color(0xFF4338CA)
                ),
                iconBg = Color(0xFF818CF8),
                onClick = onEncryptAudio
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_image,
                title = "Hide in Image",
                subtitle = "Conceal data within image files",
                gradientColors = listOf(
                    Color(0xFFEC4899),
                    Color(0xFFDB2777),
                    Color(0xFFBE185D)
                ),
                iconBg = Color(0xFFF472B6),
                onClick = onEncryptImage
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_tamper,
                title = "Tamper Check",
                subtitle = "Verify file integrity",
                gradientColors = listOf(
                    Color(0xFF10B981),
                    Color(0xFF059669),
                    Color(0xFF047857)
                ),
                iconBg = Color(0xFF34D399),
                onClick = onTamperCheck
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                QuickBox(
                    text = "Decrypt Audio",
                    icon = R.drawable.ic_mic,
                    gradientColors = listOf(
                        Color(0xFF0EA5E9),
                        Color(0xFF0284C7),
                        Color(0xFF0369A1)
                    ),
                    iconBg = Color(0xFF38BDF8),
                    onClick = onDecryptAudio,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(12.dp))

                QuickBox(
                    text = "Decrypt Image",
                    icon = R.drawable.ic_image,
                    gradientColors = listOf(
                        Color(0xFFF59E0B),
                        Color(0xFFD97706),
                        Color(0xFFB45309)
                    ),
                    iconBg = Color(0xFFFBBF24),
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
fun DashboardTile(
    icon: Int, 
    title: String, 
    subtitle: String, 
    gradientColors: List<Color>,
    iconBg: Color,
    onClick: () -> Unit
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
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp, pressedElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(iconBg.copy(alpha = 0.9f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }

                Spacer(Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )
                }

                Spacer(Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.9f))
                )
            }
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
    gradientColors: List<Color>,
    iconBg: Color,
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
            stiffness = Spring.StiffnessMedium
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp, pressedElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(iconBg.copy(alpha = 0.9f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}