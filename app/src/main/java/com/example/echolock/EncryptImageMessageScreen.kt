package com.example.echolock.ui.screens

import androidx.compose.ui.text.TextStyle
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.ImageSteganography
import com.example.echolock.util.saveStegoImage

@Composable
fun EncryptImageMessageScreen(
    imageUri: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    // Restore state from UserSession
    var message by remember { mutableStateOf(UserSession.encryptImageMessage ?: "") }
    var password by remember { mutableStateOf(UserSession.encryptImagePassword ?: "") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    
    // Save to UserSession as user types
    LaunchedEffect(message) {
        UserSession.encryptImageMessage = message
    }
    
    LaunchedEffect(password) {
        UserSession.encryptImagePassword = password
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

        /* ---------- TOP BAR ---------- */
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
                text = "Secret Message",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- INFO CARD ---------- */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFFFF9)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = "Your message will be securely hidden inside the selected image using LSB steganography.",
                fontSize = 14.sp,
                color = AppColors.PrimaryDark,
                modifier = Modifier.padding(16.dp),
                lineHeight = 20.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        /* ---------- MESSAGE INPUT ---------- */
        Text(
            text = "Secret Message",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = {
                Text(
                    "Type your secret message here…",
                    color = AppColors.TextTertiary
                )
            },
            shape = RoundedCornerShape(14.dp),
            maxLines = 6,
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
            )
        )

        Spacer(Modifier.height(24.dp))

        /* ---------- PASSWORD INPUT ---------- */
        Text(
            text = "Enter Password",
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
                    "Enter password to protect your message",
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

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        /* ---------- ACTION BUTTON ---------- */
        val buttonEnabled by remember { derivedStateOf { message.isNotBlank() && password.isNotBlank() && !loading } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )
        
        Button(
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
            ),
            onClick = {

                if (message.isBlank()) {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password.isBlank()) {
                    Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true

                try {
                    // Get original file name from HistoryTempStore (set during image upload)
                    // If not available, try to get from URI or use a fallback
                    val originalFileName = com.example.echolock.util.HistoryTempStore.lastImageFileName
                        ?: try {
                            val uri = Uri.parse(imageUri)
                            val fileName = uri.lastPathSegment ?: "image_${System.currentTimeMillis()}.png"
                            // Store it for future use
                            com.example.echolock.util.HistoryTempStore.lastImageFileName = fileName
                            fileName
                        } catch (e: Exception) {
                            "image_${System.currentTimeMillis()}.png"
                        }

                    val uri = Uri.parse(imageUri)
                    val stream = context.contentResolver.openInputStream(uri)
                        ?: throw Exception("Cannot open image")

                    val bitmap = BitmapFactory.decodeStream(stream)
                        ?: throw Exception("Image decode failed")

                    // Generate a local identifier (hash of image + timestamp)
                    val imageHash = java.security.MessageDigest.getInstance("MD5")
                        .digest((originalFileName + System.currentTimeMillis()).toByteArray())
                        .joinToString("") { "%02x".format(it) }
                        .take(16) // Use first 16 characters as identifier

                    /* ================= ENCRYPT (STEP 1: Message Only) ================= */
                    // First, encode the message without signature
                    val stegoBitmapStep1 = ImageSteganography.encode(
                        original = bitmap,
                        identifier = imageHash,
                        message = message,
                        password = password,
                        originalFileName = originalFileName,
                        integritySignature = null // No signature yet
                    )

                    // Generate integrity signature from encrypted image (before embedding signature)
                    val hashAndSignature = com.example.echolock.util.IntegritySignatureUtil.generateHashAndSignature(stegoBitmapStep1)
                    
                    if (hashAndSignature != null) {
                        val (imageHashValue, integritySignature) = hashAndSignature
                        android.util.Log.d("EncryptImage", "Generated hash: $imageHashValue")
                        android.util.Log.d("EncryptImage", "Generated signature: $integritySignature")
                        
                        /* ================= ENCRYPT (STEP 2: Embed Signature) ================= */
                        // Now embed the signature in the already-encrypted image
                        val (finalStegoBitmap, signatureStartIndex) = com.example.echolock.util.ImageSteganography.encodeWithSignature(
                            encryptedBitmap = stegoBitmapStep1,
                            integritySignature = integritySignature
                        )
                        
                        // Store signature metadata for verification (optional - signature is embedded in image)
                        android.util.Log.d("EncryptImage", "Signature embedded at pixel index: $signatureStartIndex")
                        
                        saveStegoImage(context, finalStegoBitmap, originalFileName)
                    } else {
                        android.util.Log.w("EncryptImage", "Failed to generate integrity signature, saving without signature")
                        saveStegoImage(context, stegoBitmapStep1, originalFileName)
                    }

                    loading = false
                    onSuccess()

                } catch (e: Exception) {
                    loading = false
                    android.util.Log.e("EncryptImage", "Encryption failed: ${e.message}", e)
                    Toast.makeText(
                        context,
                        "Encryption failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        ) {
            Text(
                text = if (loading) "Encrypting…" else "Encrypt & Hide",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
