package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.ui.theme.AppColors

@Composable
fun SettingsScreen(
    onBack: () -> Unit,

    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAboutClick: () -> Unit,
    onFaqClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAppThemeClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFilesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onFileStorageClick: () -> Unit,      // ✅ ADD
    onNotificationsClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(3) }

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
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(28.dp).clickable { onBack() },
                tint = AppColors.TextPrimary
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            item {
                SettingsSection("Account") {
                    SettingItem(Icons.Filled.Person, "Edit Profile", onEditProfileClick)
                    SettingItem(Icons.Filled.Lock, "Change Password", onChangePasswordClick)
                }
            }

            item {
                SettingsSection("Preferences") {
                    SettingItem(
                        Icons.Filled.DarkMode,
                        "App Theme",
                        onClick = { onAppThemeClick() }
                    )

                    SettingItem(
                        Icons.Filled.Folder,
                        "File Storage",
                        onClick = { onFileStorageClick() }
                    )

                    SettingItem(
                        Icons.Filled.Notifications,
                        "Notifications",
                        onClick = { onNotificationsClick() }
                    )

                }
            }

            item {
                SettingsSection("Security & Support") {
                    SettingItem(Icons.Filled.Info, "About EchoLock", onAboutClick)
                    SettingItem(Icons.Filled.Help, "FAQ", onFaqClick)
                }
            }

            item {
                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { onLogoutClick() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAEA))
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Logout, null, tint = Color(0xFFE53935))
                        Spacer(Modifier.width(14.dp))
                        Text("Log Out", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(Modifier.height(22.dp))
                Text(
                    "EchoLock v1.0.4 (Build 2025)",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(Modifier.height(22.dp))
            }
        }

        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                when (it) {
                    0 -> onHomeClick()
                    1 -> onFilesClick()
                    2 -> onHistoryClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextSecondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.Surface)
        ) {
            Column(Modifier.padding(12.dp)) { content() }
        }
    }
}

@Composable
fun SettingItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit = {}) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(icon, null, tint = AppColors.PrimaryDark, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Filled.KeyboardArrowRight, null, tint = AppColors.TextSecondary)
    }
}
