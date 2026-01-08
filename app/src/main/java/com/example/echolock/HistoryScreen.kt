package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolock.R
import com.example.echolock.model.HistoryItem
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors
import com.example.echolock.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onFilesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {

    val today by viewModel.today
    val yesterday by viewModel.yesterday
    val loading by viewModel.loading

    var selectedTab by remember { mutableStateOf(2) }

    // Load history from backend
    LaunchedEffect(Unit) {
        val userId = com.example.echolock.session.UserSession.userId.toIntOrNull() ?: 1
        viewModel.loadHistory(userId = userId)
    }

    // Animation for screen entrance
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
    ) {
        /* ---------------- CONTENT AREA ---------------- */
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            /* ---------------- TOP BAR ---------------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() },
                    tint = Color.White
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "History",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.height(18.dp))

            /* ---------------- LIST ---------------- */
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    if (today.isNotEmpty()) {
                        item { SectionTitle("Today") }
                        items(today) { item ->
                            HistoryCard(item)
                        }
                    }

                    if (yesterday.isNotEmpty()) {
                        item { SectionTitle("Yesterday") }
                        items(yesterday) { item ->
                            HistoryCard(item)
                        }
                    }
                }
            }
        }

        /* ---------------- BOTTOM NAV ---------------- */
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                when (it) {
                    0 -> onHomeClick()
                    1 -> onFilesClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }
}

/* ---------------- SECTION TITLE ---------------- */
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFFBEE7E8),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/* ---------------- HISTORY CARD ---------------- */
@Composable
fun HistoryCard(item: HistoryItem) {

    // Determine file type from action or file extension
    val isImageFile = item.action.contains("Image", ignoreCase = true) ||
            item.fileName.endsWith(".png", ignoreCase = true) ||
            item.fileName.endsWith(".jpg", ignoreCase = true) ||
            item.fileName.endsWith(".jpeg", ignoreCase = true) ||
            item.fileName.endsWith(".webp", ignoreCase = true)

    val isAudioFile = item.action.contains("Audio", ignoreCase = true) ||
            item.fileName.endsWith(".wav", ignoreCase = true) ||
            item.fileName.endsWith(".mp3", ignoreCase = true) ||
            item.fileName.endsWith(".m4a", ignoreCase = true)

    // Format action text
    val formattedAction = remember(item.action) {
        when {
            item.action.contains("Encrypted", ignoreCase = true) -> "Encryption Successful"
            item.action.contains("Decrypted", ignoreCase = true) -> "Decryption Successful"
            item.action.contains("Tamper Check", ignoreCase = true) -> item.action // Keep tamper check as is
            else -> item.action // Keep other actions as is
        }
    }

    val cardColor = when {
        isImageFile -> FeatureCardColors.Orange.copy(alpha = 0.9f)
        isAudioFile -> FeatureCardColors.Purple.copy(alpha = 0.9f)
        else -> FeatureCardColors.Blue.copy(alpha = 0.9f)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Icon with circular background - colorful based on file type
            val iconColor = when {
                isImageFile -> FeatureCardColors.Orange
                isAudioFile -> FeatureCardColors.Purple
                else -> FeatureCardColors.Blue
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        iconColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isImageFile -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_image),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    isAudioFile -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.fileName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "$formattedAction • ${item.time}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
        }
    }
}