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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors
import com.example.echolock.util.HistoryTempStore
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
    onContinue: (String) -> Unit   // ✅ WILL PASS IMAGE URI STRING
) {
    val context = LocalContext.current

    // Restore state from UserSession
    var selectedImageUri by remember {
        val uriString = UserSession.selectedImageUriString
        mutableStateOf<Uri?>(
            if (uriString != null) Uri.parse(uriString) else null
        )
    }
    var uploadedImageUri by remember {
        val uriString = UserSession.selectedImageUriString
        mutableStateOf<Uri?>(
            if (uriString != null) Uri.parse(uriString) else null
        )
    }
    var isUploading by remember { mutableStateOf(false) }

    /* ---------------- IMAGE UPLOAD (OPTIONAL BACKEND) ---------------- */
    fun uploadImage(uri: Uri) {
        isUploading = true

        val file = uriToFile(context, uri)
        HistoryTempStore.lastImageFileName = file.name
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val requestBody = file.asRequestBody(mimeType.toMediaType())

        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = file.name,
            body = requestBody
        )

        val userId = UserSession.userId.ifEmpty { "1" }
            .toRequestBody("text/plain".toMediaType())

        RetrofitClient.instance.uploadImage(imagePart, userId)
            .enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    isUploading = false
                    // ✅ Upload success — we DO NOT need image_name anymore
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    isUploading = false
                }
            })
    }

    /* ---------------- IMAGE PICKER ---------------- */
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                uploadedImageUri = uri
                // Store UI state
                UserSession.selectedImageUriString = uri.toString()
                uploadImage(uri) // optional
            }
        }

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    /* ---------------- UI ---------------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        // Header
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
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Select Image",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(28.dp))

        if (uploadedImageUri == null) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(2.dp, FeatureCardColors.Pink, RoundedCornerShape(16.dp))
                    .background(FeatureCardColors.Pink.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
                    .clickable(enabled = !isUploading) {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "Upload",
                        modifier = Modifier.size(56.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Tap to Upload Image",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (isUploading) "Uploading..." else "Supports PNG, JPG, WEBP",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

        } else {

            Text(
                text = "Selected Image Preview",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            AsyncImage(
                model = uploadedImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(AppColors.BorderLight, RoundedCornerShape(16.dp))
            )

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    uploadedImageUri = null
                    selectedImageUri = null
                    UserSession.selectedImageUriString = null
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Re-select Image", fontSize = 15.sp)
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        // Continue Button
        val buttonEnabled by remember { derivedStateOf { uploadedImageUri != null && !isUploading } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            onClick = {
                uploadedImageUri?.let {
                    onContinue(it.toString())
                }
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FeatureCardColors.Pink,
                disabledContainerColor = Color(0xFF475569)
            ),
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

        Spacer(Modifier.height(8.dp))
    }
}