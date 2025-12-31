package com.example.echolock.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors

@Composable
fun DecryptImageScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit   // ✅ pass image URI
) {
    val context = LocalContext.current
    // Restore state from UserSession
    var selectedImageUri by remember { 
        mutableStateOf<Uri?>(
            UserSession.decryptImageUriString?.let { Uri.parse(it) }
        )
    }
    var password by remember { mutableStateOf(UserSession.decryptImagePassword ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Save to UserSession as user types
    LaunchedEffect(password) {
        UserSession.decryptImagePassword = password
    }

    /* ---------- IMAGE PICKER ---------- */
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
            // Store UI state
            UserSession.decryptImageUriString = uri?.toString()
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
                "Decrypt Image",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- IMAGE PICK / PREVIEW ---------- */
        if (selectedImageUri == null) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(2.dp, AppColors.BorderLight, RoundedCornerShape(16.dp))
                    .background(AppColors.Surface, RoundedCornerShape(16.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painterResource(R.drawable.ic_upload),
                        contentDescription = null,
                        tint = AppColors.PrimaryDark,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Select Encrypted Image",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = AppColors.TextPrimary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Tap to extract hidden message",
                        fontSize = 13.sp,
                        color = AppColors.TextSecondary
                    )
                }
            }

        } else {

            Text(
                "Selected Image",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = AppColors.TextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(AppColors.BorderLight, RoundedCornerShape(16.dp))
                    .clickable {
                        selectedImageUri = null
                        UserSession.decryptImageUriString = null
                        imagePickerLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Tap image to reselect",
                color = AppColors.PrimaryDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    selectedImageUri = null
                    UserSession.decryptImageUriString = null
                    imagePickerLauncher.launch("image/*")
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- PASSWORD FIELD (SHOW WHEN IMAGE SELECTED) ---------- */
        AnimatedVisibility(
            visible = selectedImageUri != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Text(
                    "Enter Password",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = AppColors.TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

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
        val buttonEnabled by remember { derivedStateOf { selectedImageUri != null && password.isNotBlank() } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )
        
        Button(
            onClick = {
                onContinue(selectedImageUri.toString())
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryDark,
                disabledContainerColor = AppColors.BorderLight
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                "Extract Message",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(Modifier.height(8.dp))
    }
}
