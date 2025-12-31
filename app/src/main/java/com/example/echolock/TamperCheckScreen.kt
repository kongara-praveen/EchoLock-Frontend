package com.example.echolock.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.theme.AppColors

@Composable
fun TamperCheckScreen(
    onBack: () -> Unit,
    onStartCheck: (Uri, String?) -> Unit
) {
    val context = LocalContext.current

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    /* -------- FILE PICKER -------- */
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedUri = uri

                // 🔹 Get file name
                context.contentResolver.query(
                    uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use {
                    if (it.moveToFirst()) {
                        selectedFileName = it.getString(0)
                    }
                }
            }
        }

    // Screen entrance animation
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Tamper Check",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Back",
                color = AppColors.PrimaryDark,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(Modifier.height(30.dp))

        /* ---------- UPLOAD CARD ---------- */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.Surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (selectedUri == null) {

                    Text(
                        text = "Upload Encrypted Image or Audio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.TextPrimary
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { picker.launch("*/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Choose File", color = AppColors.TextOnPrimary, fontWeight = FontWeight.SemiBold)
                    }

                } else {

                    Text(
                        text = "File Selected",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.TextPrimary
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = selectedFileName ?: "Selected file",
                        fontSize = 13.sp,
                        color = AppColors.TextSecondary
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            selectedUri = null
                            selectedFileName = null
                            picker.launch("*/*")
                        }
                    ) {
                        Text("Reselect File")
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        /* ---------- CHECK BUTTON ---------- */
        Button(
            onClick = {
                selectedUri?.let { onStartCheck(it, selectedFileName) }
            },
            enabled = selectedUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
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
            Text("Check Tamper", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
