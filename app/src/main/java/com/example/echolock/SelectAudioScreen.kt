package com.example.echolock.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import com.example.echolock.api.AudioItem
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RecentAudioResponse
import com.example.echolock.api.RetrofitClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

/* ---------- URI → FILE (KEEP ORIGINAL NAME) ---------- */
fun uriToAudioFile(context: Context, uri: Uri): File {
    val resolver = context.contentResolver
    val inputStream = resolver.openInputStream(uri)
        ?: throw IllegalStateException("Cannot open audio URI")

    val fileName =
        uri.lastPathSegment?.substringAfterLast("/") ?: "audio_file"

    val file = File(context.cacheDir, fileName)

    FileOutputStream(file).use { output ->
        inputStream.copyTo(output)
    }

    inputStream.close()
    return file
}

@Composable
fun SelectAudioScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val context = LocalContext.current

    var selectedAudioUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedAudioUri by remember { mutableStateOf<Uri?>(null) }
    var recentAudios by remember { mutableStateOf<List<AudioItem>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }

    /* ---------- Load recent audio ---------- */
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
                    Log.e("AUDIO_UPLOAD", "Load recent failed", t)
                }
            })
    }

    LaunchedEffect(Unit) { loadRecentAudio() }

    /* ---------- Upload audio ---------- */
    fun uploadAudio(uri: Uri) {
        Log.e("AUDIO_UPLOAD", "uploadAudio() CALLED")
        isUploading = true

        val file = uriToAudioFile(context, uri)
        val mimeType =
            context.contentResolver.getType(uri) ?: "audio/mpeg"

        val requestBody =
            file.asRequestBody(mimeType.toMediaType())

        val audioPart =
            MultipartBody.Part.createFormData(
                "audio",
                file.name,
                requestBody
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
                    if (response.body()?.status == "success") {
                        uploadedAudioUri = uri   // 🔒 lock upload
                        loadRecentAudio()
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    isUploading = false
                    Log.e("AUDIO_UPLOAD", "FAILED", t)
                }
            })
    }

    /* ---------- File picker ---------- */
    val audioPickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {
                selectedAudioUri = uri
                uploadAudio(uri)
            }
        }

    /* ---------- UI ---------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Select Audio", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(25.dp))

        /* ---------- CONDITIONAL UI ---------- */
        if (uploadedAudioUri == null) {

            // 🔓 Upload allowed
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEAF7FB), RoundedCornerShape(16.dp))
                    .clickable(enabled = !isUploading) {
                        audioPickerLauncher.launch(
                            arrayOf(
                                "audio/mpeg",
                                "audio/wav",
                                "audio/flac",
                                "audio/aac"
                            )
                        )
                    }
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = "",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text("Tap to Upload Audio", fontSize = 16.sp)
                Text(
                    if (isUploading) "Uploading..." else "Supports MP3, WAV, FLAC, AAC",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

        } else {

            // 🔒 Upload disabled — show uploaded audio
            Text(
                "Uploaded Audio",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF005F73)
            )

            Spacer(Modifier.height(10.dp))

            AudioListItem(
                fileName = uploadedAudioUri!!.lastPathSegment ?: "Audio file",
                size = "Uploaded successfully"
            )

            Spacer(Modifier.height(12.dp))

            // 🔁 Reselect audio
            Button(
                onClick = {
                    uploadedAudioUri = null
                    selectedAudioUri = null
                },
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text("Reselect Audio")
            }
        }

        Spacer(Modifier.height(25.dp))

        // Recent files
        Text("Recent Files", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Spacer(Modifier.height(12.dp))

        if (recentAudios.isEmpty()) {
            Text("No recent audio files", fontSize = 13.sp, color = Color.Gray)
        } else {
            recentAudios.forEach {
                AudioListItem(
                    fileName = it.fileName,
                    size = "${it.size} • ${it.time}"
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = uploadedAudioUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Continue", color = Color.White)
        }
    }
}

@Composable
fun AudioListItem(
    fileName: String,
    size: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "",
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(fileName, fontWeight = FontWeight.Bold)
            Text(size, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
