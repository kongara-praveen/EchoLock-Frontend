package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.util.NotificationPrefs
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val enabled by NotificationPrefs.isEnabled(context).collectAsState(initial = true)
    val encryptionEnabled by NotificationPrefs.isEncryptionEnabled(context).collectAsState(initial = true)
    val decryptionEnabled by NotificationPrefs.isDecryptionEnabled(context).collectAsState(initial = true)
    val tamperEnabled by NotificationPrefs.isTamperEnabled(context).collectAsState(initial = true)
    val soundEnabled by NotificationPrefs.isSoundEnabled(context).collectAsState(initial = true)
    val vibrationEnabled by NotificationPrefs.isVibrationEnabled(context).collectAsState(initial = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF4FBFF), Color(0xFFE6F4FA))
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
                Text("Notifications", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Toggle
                item {
                    NotificationCard(
                        icon = Icons.Default.Notifications,
                        title = "Enable Notifications",
                        subtitle = "Master switch for all notifications",
                        checked = enabled,
                        onCheckedChange = {
                            scope.launch {
                                NotificationPrefs.setEnabled(context, it)
                            }
                        }
                    )
                }

                // Sub-settings (only shown when main toggle is on)
                if (enabled) {
                    item {
                        Text(
                            "Notification Types",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF005F73),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        NotificationCard(
                            icon = Icons.Default.Lock,
                            title = "Encryption Notifications",
                            subtitle = "Get notified when encryption completes",
                            checked = encryptionEnabled,
                            onCheckedChange = {
                                scope.launch {
                                    NotificationPrefs.setEncryptionEnabled(context, it)
                                }
                            }
                        )
                    }

                    item {
                        NotificationCard(
                            icon = Icons.Default.LockOpen,
                            title = "Decryption Notifications",
                            subtitle = "Get notified when decryption completes",
                            checked = decryptionEnabled,
                            onCheckedChange = {
                                scope.launch {
                                    NotificationPrefs.setDecryptionEnabled(context, it)
                                }
                            }
                        )
                    }

                    item {
                        NotificationCard(
                            icon = Icons.Default.Warning,
                            title = "Tamper Check Alerts",
                            subtitle = "Get notified about tamper check results",
                            checked = tamperEnabled,
                            onCheckedChange = {
                                scope.launch {
                                    NotificationPrefs.setTamperEnabled(context, it)
                                }
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Notification Settings",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF005F73),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        NotificationCard(
                            icon = Icons.Default.VolumeUp,
                            title = "Sound",
                            subtitle = "Play sound for notifications",
                            checked = soundEnabled,
                            onCheckedChange = {
                                scope.launch {
                                    NotificationPrefs.setSoundEnabled(context, it)
                                }
                            }
                        )
                    }

                    item {
                        NotificationCard(
                            icon = Icons.Default.PhoneAndroid,
                            title = "Vibration",
                            subtitle = "Vibrate for notifications",
                            checked = vibrationEnabled,
                            onCheckedChange = {
                                scope.launch {
                                    NotificationPrefs.setVibrationEnabled(context, it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                tint = Color(0xFF005F73),
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF0A2E45)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7E80)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF005F73),
                    checkedTrackColor = Color(0xFF4A9FB8)
                )
            )
        }
    }
}
