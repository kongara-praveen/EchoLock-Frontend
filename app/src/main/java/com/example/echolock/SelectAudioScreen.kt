package com.example.echolock.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.*
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

@Composable
fun SelectAudioScreen(
    onBack: () -> Unit,
    onGoEncrypt: () -> Unit,
    onGoConvert: () -> Unit
) {
    val context = LocalContext.current

    // Restore state from UserSession
    var selectedFile by remember { mutableStateOf<File?>(null) }
    var selectedUri by remember {
        mutableStateOf<Uri?>(
            UserSession.selectedAudioUriString?.let { Uri.parse(it) }
        )
    }
    var selectedFileName by remember {
        mutableStateOf<String?>(UserSession.originalAudioName)
    }
    var recentAudios by remember { mutableStateOf<List<AudioItem>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }

    // Restore file if URI exists (only on initial load)
    LaunchedEffect(Unit) {
        selectedUri?.let { uri ->
            try {
                val name = getFileName(context, uri)
                val file = File(context.cacheDir, name)
                if (!file.exists()) {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                if (file.exists()) {
                    selectedFile = file
                    selectedFileName = name
                }
            } catch (e: Exception) {
                // File might not be accessible, clear state
                selectedUri = null
                selectedFile = null
                selectedFileName = null
                UserSession.selectedAudioUriString = null
            }
        }
    }

    /* ---------- LOAD RECENT FILES ---------- */
    fun loadRecentAudio() {
        val userId = UserSession.userId.toIntOrNull() ?: 1
        RetrofitClient.instance.getRecentAudio(userId)
            .enqueue(object : Callback<RecentAudioResponse> {
                override fun onResponse(
                    call: Call<RecentAudioResponse>,
                    response: Response<RecentAudioResponse>
                ) {
                    if (response.isSuccessful) {
                        recentAudios = response.body()?.data ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<RecentAudioResponse>, t: Throwable) {
                    Log.e("RECENT_AUDIO", "Failed", t)
                }
            })
    }

    LaunchedEffect(Unit) { loadRecentAudio() }

    /* ---------- FILE PICKER ---------- */
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri ?: return@rememberLauncherForActivityResult

            val name = getFileName(context, uri)
            val file = File(context.cacheDir, name)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            selectedFile = file
            selectedUri = uri
            selectedFileName = name

            // ✅ STORE ORIGINAL AUDIO INFO AND UI STATE
            UserSession.originalAudioPath = file.absolutePath
            UserSession.originalAudioName = name
            UserSession.selectedAudioUriString = uri.toString()
        }

    /* ---------- UPLOAD ORIGINAL AUDIO ---------- */
    fun uploadOriginalAudioAndNavigate() {
        val file = selectedFile ?: return
        val uri = selectedUri ?: return

        isUploading = true

        val mimeType =
            context.contentResolver.getType(uri) ?: "audio/mpeg"

        val requestBody =
            file.asRequestBody(mimeType.toMediaType())

        val audioPart =
            MultipartBody.Part.createFormData(
                name = "audio",
                filename = file.name,
                body = requestBody
            )

        val userId = UserSession.userId.ifEmpty { "1" }
            .toRequestBody("text/plain".toMediaType())

        RetrofitClient.instance.uploadAudio(audioPart, userId)
            .enqueue(object : Callback<GenericResponse> {

                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    isUploading = false

                    if (response.isSuccessful && response.body()?.status == "success") {
                        loadRecentAudio()

                        // ✅ WAV vs NON-WAV DECISION
                        if (file.name.lowercase().endsWith(".wav")) {
                            onGoEncrypt()
                        } else {
                            onGoConvert()
                        }
                    } else {
                        Log.e("UPLOAD", "Upload failed")
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    isUploading = false
                    Log.e("UPLOAD", "Failed", t)
                }
            })
    }

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
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
                "Select Audio",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))

        AnimatedVisibility(
            visible = selectedFile == null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FeatureCardColors.Purple.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                    .border(2.dp, FeatureCardColors.Purple, RoundedCornerShape(16.dp))
                    .clickable { picker.launch(arrayOf("audio/*")) }
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Tap to Upload Audio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Supports MP3, WAV, FLAC, AAC",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        AnimatedVisibility(
            visible = selectedFile != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                AudioListItem(
                    fileName = selectedFileName ?: "Audio",
                    size = if (isUploading) "Uploading…" else "Ready"
                )

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        selectedFile = null
                        selectedUri = null
                        selectedFileName = null
                        UserSession.selectedAudioUriString = null
                        UserSession.originalAudioPath = null
                        UserSession.originalAudioName = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppColors.TextSecondary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Reselect Audio", fontSize = 15.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Recent Files",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(Modifier.height(12.dp))

        recentAudios.forEach {
            AudioListItem(
                fileName = it.fileName,
                size = "${it.size} • ${it.time}"
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        val buttonEnabled by remember { derivedStateOf { selectedFile != null && !isUploading } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            onClick = { uploadOriginalAudioAndNavigate() },
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
            Text("Continue", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/* ---------- HELPERS ---------- */

fun getFileName(context: Context, uri: Uri): String {
    context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null
    )?.use {
        if (it.moveToFirst()) return it.getString(0)
    }
    return "audio_${System.currentTimeMillis()}"
}

@Composable
fun AudioListItem(fileName: String, size: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FeatureCardColors.Purple.copy(alpha = 0.9f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_music),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color.White
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                fileName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                size,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}