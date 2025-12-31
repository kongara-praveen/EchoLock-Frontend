package com.example.echolock.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.HistoryTempStore

@Composable
fun DecryptAudioScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {

    // Restore state from UserSession
    var selectedAudioUri by remember { 
        mutableStateOf<Uri?>(
            UserSession.decryptAudioUriString?.let { Uri.parse(it) }
        )
    }
    var password by remember { mutableStateOf(UserSession.decryptionPassword ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Save to UserSession as user types
    LaunchedEffect(password) {
        UserSession.decryptionPassword = password
    }

    /* ---------- File Picker ---------- */
    val audioPickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                selectedAudioUri = it
                UserSession.decryptAudioUri = it.toString()
                UserSession.decryptAudioUriString = it.toString()  // Store for UI state
                // Store file name for history
                HistoryTempStore.lastAudioFileName = uri.lastPathSegment ?: "audio_file"
            }
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
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() },
                tint = AppColors.TextPrimary
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Decrypt Audio",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- AUDIO SELECTED / UPLOAD ---------- */
        AnimatedVisibility(
            visible = selectedAudioUri == null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(AppColors.Surface, RoundedCornerShape(16.dp))
                    .border(2.dp, AppColors.BorderLight, RoundedCornerShape(16.dp))
                    .clickable {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = AppColors.PrimaryDark
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Select Encrypted Audio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Upload audio to extract message",
                        fontSize = 13.sp,
                        color = AppColors.TextSecondary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedAudioUri != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                // ✅ Selected File UI
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.Surface, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = AppColors.PrimaryDark
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Encrypted Audio Selected",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AppColors.TextPrimary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = selectedAudioUri?.lastPathSegment
                                    ?: "audio file",
                                fontSize = 13.sp,
                                color = AppColors.TextSecondary
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Reselect Audio",
                        color = AppColors.PrimaryDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            selectedAudioUri = null
                            password = ""
                            UserSession.decryptAudioUri = null
                            UserSession.decryptAudioUriString = null
                            UserSession.decryptionPassword = null
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                /* ---------- PASSWORD FIELD (SHOW WHEN AUDIO SELECTED) ---------- */
                Text(
                    "Enter Password",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = AppColors.TextPrimary
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Enter password to decrypt",
                            color = AppColors.TextTertiary
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = AppColors.TextSecondary
                            )
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = AppColors.TextPrimary,
                        fontWeight = FontWeight.Normal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = AppColors.TextPrimary,
                        unfocusedTextColor = AppColors.TextPrimary,
                        focusedBorderColor = AppColors.PrimaryDark,
                        unfocusedBorderColor = AppColors.BorderLight,
                        cursorColor = AppColors.PrimaryDark,
                        focusedPlaceholderColor = AppColors.TextTertiary,
                        unfocusedPlaceholderColor = AppColors.TextTertiary
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        /* ---------- CONTINUE ---------- */
        val buttonEnabled by remember { derivedStateOf { selectedAudioUri != null && password.isNotBlank() } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            onClick = {
                UserSession.decryptionPassword = password
                onContinue()
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryDark,
                disabledContainerColor = AppColors.BorderLight
            ),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                "Continue",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
