package com.example.echolock.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.RetrofitClient
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.HistoryTempStore
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.echolock.ui.theme.FeatureCardColors
import com.example.echolock.ui.theme.GradientBackgrounds

@Composable
fun DecryptAudioResultScreen(
    onDone: () -> Unit
) {
    val context = LocalContext.current

    val decryptedMessageRaw = UserSession.decryptedMessage ?: "No hidden message found"
    val decryptedMessage = if (decryptedMessageRaw == "Wrong password" || decryptedMessageRaw == "Password required") {
        decryptedMessageRaw
    } else {
        decryptedMessageRaw
    }
    val isPasswordError = decryptedMessage == "Wrong password" || decryptedMessage == "Password required"

    // Save history when screen appears (only if successful)
    LaunchedEffect(Unit) {
        val message = UserSession.decryptedMessage ?: ""
        // Don't save history if password error
        if (message == "Wrong password" || message == "Password required") {
            return@LaunchedEffect
        }
        
        val audioFileName = HistoryTempStore.lastAudioFileName ?: "audio_file"
        val userId = UserSession.userId.toIntOrNull() ?: 1
        
        try {
            withContext(Dispatchers.IO) {
                RetrofitClient.instance.addHistory(
                    userId = userId,
                    fileName = audioFileName,
                    action = "Decrypted Audio"
                )
            }
            // Clear after saving
            HistoryTempStore.lastAudioFileName = null
        } catch (e: Exception) {
            e.printStackTrace()
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
            .background(GradientBackgrounds.PrimaryGradient)

            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onDone() },
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Message Extracted",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(24.dp))

        // SUCCESS ROW
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        FeatureCardColors.Blue.copy(alpha = 0.25f),
                        RoundedCornerShape(50)
                    )
                ,
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isPasswordError) AppColors.Error else AppColors.Success
                )
            }

            Spacer(Modifier.width(14.dp))

            Text(
                text = "Decryption Result",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))

        // MESSAGE BOX
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    if (isPasswordError)
                        AppColors.Error.copy(alpha = 0.2f)
                    else
                        FeatureCardColors.Blue.copy(alpha = 0.9f)

            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    if (isPasswordError) "ERROR" else "EXTRACTED MESSAGE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isPasswordError) AppColors.Error else Color.White

                )

                Spacer(Modifier.height(14.dp))

                Text(
                    decryptedMessage,
                    fontSize = 15.sp,
                    color = if (isPasswordError) AppColors.Error else AppColors.TextPrimary
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        // COPY BUTTON (DISABLED FOR PASSWORD ERRORS)
        OutlinedButton(
            onClick = {
                if (isPasswordError) return@OutlinedButton
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE)
                            as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "Decrypted Message",
                        decryptedMessage
                    )
                )
            },
            enabled = !isPasswordError,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_copy),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Copy to Clipboard",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(12.dp))

        // DONE BUTTON
        Button(
            onClick = {
                // Clear decryption session
                UserSession.clearAudioDecryptionSession()
                onDone()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FeatureCardColors.Blue
            ),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text("Done", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
