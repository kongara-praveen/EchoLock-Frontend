package com.example.echolock.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
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

    /* ---------- STATE ---------- */
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

    /* ---------- FIELD COLORS (IMPORTANT FIX) ---------- */
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF4E5D94),
        unfocusedBorderColor = Color(0xFF9AA4B2),
        focusedLabelColor = Color(0xFF4E5D94),
        unfocusedLabelColor = Color(0xFF6D7F85),
        cursorColor = Color(0xFF4E5D94),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black
    )

    /* ---------- BACKGROUND ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF4FBFF), Color(0xFFE6F4FA))
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            /* ---------- HEADER ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
                Text("Edit Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /* ---------- PROFILE IMAGE ---------- */
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE6EEF2))
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
                                tint = Color(0xFF005F73),
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    /* ---------- FULL NAME ---------- */
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    /* ---------- EMAIL ---------- */
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = fieldColors,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    /* ---------- PASSWORD VERIFY ---------- */
                    if (editMode && !passwordVerified) {

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Confirm Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = fieldColors,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )

                        Spacer(Modifier.height(12.dp))

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
                                                Toast.makeText(context, "Password verified", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {}
                                    })
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Verify Password")
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    /* ---------- ACTION BUTTON ---------- */
                    if (!editMode) {
                        Button(
                            onClick = { editMode = true },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = CircleShape
                        ) {
                            Text("Edit Profile")
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
                                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                            onBack()
                                        }
                                    }
                                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                        saving = false
                                    }
                                })
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = CircleShape
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}
