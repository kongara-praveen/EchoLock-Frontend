package com.example.echolock.ui.screens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.ProfileResponse
import com.example.echolock.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EditProfileScreen(
    userEmail: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(userEmail) }
    var loading by remember { mutableStateOf(true) }

    val context = LocalContext.current

    /* LOAD PROFILE */
    LaunchedEffect(userEmail) {

        if (userEmail.isBlank()) {
            Toast.makeText(context, "Invalid session", Toast.LENGTH_SHORT).show()
            onBack()
            return@LaunchedEffect
        }

        RetrofitClient.instance.getProfile(userEmail)
            .enqueue(object : Callback<ProfileResponse> {

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    loading = false
                    val body = response.body()

                    if (body?.status == "success") {
                        name = body.name
                        email = body.email
                    } else {
                        Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    loading = false
                    Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            })
    }

    /* LOADING UI */
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    /* MAIN UI */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {

        Text(
            text = "Edit Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF0A2E45) // 🔥 BRIGHT TEXT
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0A2E45),
                unfocusedTextColor = Color(0xFF0A2E45),
                focusedBorderColor = Color(0xFF005F73),
                unfocusedBorderColor = Color(0xFFD0DBDF),
                cursorColor = Color(0xFF005F73)
            )
        )


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF0A2E45)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color(0xFF0A2E45),
                disabledBorderColor = Color(0xFFD0DBDF)
            )
        )


        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                RetrofitClient.instance.updateProfile(name, email)
                    .enqueue(object : Callback<GenericResponse> {

                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {
                            if (response.body()?.status == "success") {
                                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                                onSave()
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                        }
                    })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text("Save Changes")
        }
    }
}
