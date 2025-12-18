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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SelectImageScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    /* ---------------- IMAGE UPLOAD ---------------- */
    fun uploadImage(uri: Uri) {
        isUploading = true

        val file = uriToFile(context, uri)

        val mimeType =
            context.contentResolver.getType(uri) ?: "image/jpeg"

        val requestBody =
            file.asRequestBody(mimeType.toMediaType())

        val imagePart =
            MultipartBody.Part.createFormData(
                "image",           // must match $_FILES['image']
                file.name,         // original filename
                requestBody
            )

        val userId =
            "1".toRequestBody("text/plain".toMediaType())

        RetrofitClient.instance.uploadImage(imagePart, userId)
            .enqueue(object : Callback<GenericResponse> {

                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    isUploading = false
                    if (response.body()?.status == "success") {
                        uploadedImageUri = uri
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    isUploading = false
                }
            })
    }

    /* ---------------- IMAGE PICKER ---------------- */
    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                uploadImage(uri)
            }
        }

    /* ---------------- UI ---------------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Select Image", fontSize = 20.sp)
        }

        Spacer(Modifier.height(26.dp))

        /* ---------------- CONTENT ---------------- */

        if (uploadedImageUri == null) {

            // Upload enabled
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(1.5.dp, Color(0xFFCCE7F0), RoundedCornerShape(16.dp))
                    .background(Color(0xFFF1FFFA), RoundedCornerShape(16.dp))
                    .clickable(enabled = !isUploading) {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "Upload",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("Tap to Upload Image", fontSize = 16.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (isUploading) "Uploading..." else "Supports PNG, JPG, WEBP",
                        fontSize = 13.sp
                    )
                }
            }

        } else {

            // Upload locked – preview shown
            Text(
                text = "Uploaded Image Preview",
                fontSize = 14.sp,
                color = Color(0xFF005F73)
            )

            Spacer(Modifier.height(10.dp))

            AsyncImage(
                model = uploadedImageUri,
                contentDescription = "Uploaded Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.height(12.dp))

            // 🔁 Re-select Image
            Button(
                onClick = {
                    uploadedImageUri = null
                    selectedImageUri = null
                    imagePickerLauncher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Re-select Image")
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = uploadedImageUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Continue", color = Color.White)
        }
    }
}
