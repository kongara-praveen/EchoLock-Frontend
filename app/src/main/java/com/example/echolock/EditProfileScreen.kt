package com.example.echolock.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    /* ---------- STATE (UNCHANGED) ---------- */
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(UserSession.email) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var password by remember { mutableStateOf("") }
    var passwordVerified by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    /* ---------- IMAGE PICKER ---------- */
    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            selectedImageUri = it
        }

    /* ---------- LOAD PROFILE ---------- */
    LaunchedEffect(Unit) {
        if (email.isNotBlank()) {
            RetrofitClient.instance.getProfile(email)
                .enqueue(object : Callback<com.example.echolock.api.ProfileResponse> {
                    override fun onResponse(
                        call: Call<com.example.echolock.api.ProfileResponse>,
                        response: Response<com.example.echolock.api.ProfileResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.status == "success") {
                            name = response.body()?.name ?: ""
                        }
                    }
                    override fun onFailure(
                        call: Call<com.example.echolock.api.ProfileResponse>,
                        t: Throwable
                    ) {}
                })
        }
    }

    // Screen entrance animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    /* ---------- TEXT FIELD COLORS ---------- */
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AppColors.PrimaryDark,
        unfocusedBorderColor = AppColors.BorderLight,
        focusedLabelColor = AppColors.PrimaryDark,
        unfocusedLabelColor = AppColors.TextTertiary,
        cursorColor = AppColors.PrimaryDark,
        focusedTextColor = AppColors.TextPrimary,
        unfocusedTextColor = AppColors.TextPrimary,
        focusedPlaceholderColor = AppColors.TextTertiary,
        unfocusedPlaceholderColor = AppColors.TextTertiary
    )

    /* ---------- BACKGROUND ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            /* ---------- HEADER ---------- */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "Edit Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
            }

            Spacer(Modifier.height(28.dp))

            /* ---------- CARD ---------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /* ---------- PROFILE IMAGE ---------- */
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(AppColors.PrimaryLight.copy(alpha = 0.2f))
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AppColors.PrimaryDark,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(26.dp))

                    /* ---------- FULL NAME ---------- */
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name", color = AppColors.TextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    /* ---------- EMAIL ---------- */
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = AppColors.TextTertiary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(22.dp))

                    /* ---------- PASSWORD VERIFY ---------- */
                    if (editMode && !passwordVerified) {

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Confirm Password", color = AppColors.TextTertiary) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = fieldColors,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = AppColors.TextPrimary,
                                fontWeight = FontWeight.Normal
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = {
                                RetrofitClient.instance.verifyPassword(email, password)
                                    .enqueue(object : Callback<GenericResponse> {
                                        override fun onResponse(
                                            call: Call<GenericResponse>,
                                            response: Response<GenericResponse>
                                        ) {
                                            if (response.body()?.status == "success") {
                                                passwordVerified = true
                                                Toast.makeText(
                                                    context,
                                                    "Password verified",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Wrong password",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {}
                                    })
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Text("Verify Password", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(Modifier.height(18.dp))
                    }

                    /* ---------- ACTION BUTTON ---------- */
                    if (!editMode) {

                        Button(
                            onClick = { editMode = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp
                            )
                        ) {
                            Text(
                                "Edit Profile",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.TextOnPrimary
                            )
                        }

                    } else if (passwordVerified) {

                        Button(
                            onClick = {
                                saving = true
                                RetrofitClient.instance.updateProfile(
                                    oldEmail = UserSession.email,
                                    email = email,
                                    fullName = name
                                ).enqueue(object : Callback<GenericResponse> {
                                    override fun onResponse(
                                        call: Call<GenericResponse>,
                                        response: Response<GenericResponse>
                                    ) {
                                        saving = false
                                        if (response.body()?.status == "success") {
                                            UserSession.email = email
                                            Toast.makeText(
                                                context,
                                                "Profile updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            onBack()
                                        }
                                    }
                                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                        saving = false
                                    }
                                })
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.PrimaryDark,
                                disabledContainerColor = AppColors.BorderLight
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp
                            ),
                            enabled = !saving
                        ) {
                            Text(
                                "Save Changes",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.TextOnPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
