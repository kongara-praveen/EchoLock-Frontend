package com.example.echolock.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.*
import com.example.echolock.session.UserSession
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

    var selectedFile by remember { mutableStateOf<File?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var recentAudios by remember { mutableStateOf<List<AudioItem>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }

    /* ---------- LOAD RECENT FILES ---------- */
    fun loadRecentAudio() {
        RetrofitClient.instance.getRecentAudio(1)
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

            // ✅ STORE ORIGINAL AUDIO INFO
            UserSession.originalAudioPath = file.absolutePath
            UserSession.originalAudioName = name
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

        val userId =
            "1".toRequestBody("text/plain".toMediaType())

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

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Select Audio", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(25.dp))

        if (selectedFile == null) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEAF7FB), RoundedCornerShape(16.dp))
                    .clickable { picker.launch(arrayOf("audio/*")) }
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text("Tap to Upload Audio", fontSize = 16.sp)
                Text(
                    "Supports MP3, WAV, FLAC, AAC",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

        } else {

            AudioListItem(
                fileName = selectedFileName ?: "Audio",
                size = if (isUploading) "Uploading…" else "Ready"
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    selectedFile = null
                    selectedUri = null
                    selectedFileName = null
                    UserSession.clearAudioEncryptionSession()
                },
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text("Reselect Audio")
            }
        }

        Spacer(Modifier.height(25.dp))

        Text("Recent Files", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))

        recentAudios.forEach {
            AudioListItem(
                fileName = it.fileName,
                size = "${it.size} • ${it.time}"
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { uploadOriginalAudioAndNavigate() },
            enabled = selectedFile != null && !isUploading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Continue", color = Color.White)
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
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_music),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(fileName, fontWeight = FontWeight.Bold)
            Text(size, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
