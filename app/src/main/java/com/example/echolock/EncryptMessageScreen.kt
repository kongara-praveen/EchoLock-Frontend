package com.example.echolock.ui.screens

import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors

@Composable
fun EncryptMessageScreen(
    onBack: () -> Unit,
    onEncrypt: () -> Unit     // 🔥 no parameter needed now
) {

    // Restore state from UserSession
    var message by remember { mutableStateOf(UserSession.secretMessage ?: "") }
    var password by remember { mutableStateOf(UserSession.encryptionPassword ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Save to UserSession as user types
    LaunchedEffect(message) {
        UserSession.secretMessage = message
    }

    LaunchedEffect(password) {
        UserSession.encryptionPassword = password
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() },
                tint = Color.White

            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Secret Message",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White

            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- INFO BOX ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.PrimaryLight.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White

            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Your message will be encrypted safely.",
                fontSize = 14.sp,
                color = Color.White

            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Enter Your Message",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color.White

        )
        Spacer(Modifier.height(8.dp))

        /* ---------- INPUT ---------- */
        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
                error = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = {
                Text(
                    "Type your secret message here...",
                    color = Color.White

                )
            },
            isError = error != null,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
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


        if (error != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = error!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Enter Password",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = Color.White

        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Enter password to protect your message",
                    color = Color.White

                )
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            isError = error != null,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
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

        Spacer(Modifier.height(8.dp))

        Text(
            text = "AES-256 Encryption",
            color = AppColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        /* ---------- ACTION ---------- */
        val buttonEnabled by remember { derivedStateOf { message.trim().isNotEmpty() && password.trim().isNotEmpty() } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            onClick = {
                val cleanMessage = message.trim()
                val cleanPassword = password.trim()

                if (cleanMessage.isEmpty()) {
                    error = "Message cannot be empty"
                } else if (cleanPassword.isEmpty()) {
                    error = "Password is required"
                } else {
                    // ✅ STORE MESSAGE AND PASSWORD FOR NEXT SCREEN
                    UserSession.secretMessage = cleanMessage
                    UserSession.encryptionPassword = cleanPassword

                    // ✅ MOVE TO EncryptionProgressScreen
                    onEncrypt()
                }
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            colors = ButtonDefaults.buttonColors(
                containerColor = FeatureCardColors.Blue,
                disabledContainerColor = Color(0xFF475569)
            ),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "Encrypt & Hide",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}