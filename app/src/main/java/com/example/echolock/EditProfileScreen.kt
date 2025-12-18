package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
    userEmail: String,   // pass logged-in email
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(userEmail) }

    val context = LocalContext.current

    // 🔹 Load profile when screen opens
    LaunchedEffect(Unit) {
        RetrofitClient.instance.getProfile(userEmail)
            .enqueue(object : Callback<ProfileResponse> {

                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    response.body()?.let {
                        if (it.status == "success") {
                            name = it.name
                            email = it.email
                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Failed to load profile",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
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
                                Toast.makeText(
                                    context,
                                    "Profile updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onSave()
                            }
                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Network error",
                                Toast.LENGTH_SHORT
                            ).show()
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
