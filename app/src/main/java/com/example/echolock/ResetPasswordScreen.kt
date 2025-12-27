package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onBack: () -> Unit,
    onResetDone: () -> Unit
) {

    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    var newPassVisible by remember { mutableStateOf(false) }
    var confirmPassVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val email = UserSession.resetEmail.trim() // ✅ REQUIRED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(Modifier.height(25.dp))

        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(26.dp)
                .clickable(enabled = !loading) { onBack() }
        )

        Spacer(Modifier.height(25.dp))

        Text(
            "Reset Password",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF082B34)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Create a new strong password for your account.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(Modifier.height(28.dp))

        // 🔐 NEW PASSWORD
        OutlinedTextField(
            value = newPass,
            onValueChange = { newPass = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (newPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (newPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Color(0xFF005F73),
                    modifier = Modifier.clickable(enabled = !loading) {
                        newPassVisible = !newPassVisible
                    }
                )
            }
        )

        Spacer(Modifier.height(14.dp))

        // 🔐 CONFIRM PASSWORD
        OutlinedTextField(
            value = confirmPass,
            onValueChange = { confirmPass = it },
            label = { Text("Confirm New Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (confirmPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Color(0xFF005F73),
                    modifier = Modifier.clickable(enabled = !loading) {
                        confirmPassVisible = !confirmPassVisible
                    }
                )
            }
        )

        Spacer(Modifier.height(30.dp))

        // 🔘 RESET BUTTON
        Button(
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            onClick = {

                val cleanNewPass = newPass.trim()
                val cleanConfirmPass = confirmPass.trim()

                if (email.isBlank()) {
                    Toast.makeText(context, "Session expired. Try again.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (cleanNewPass.isBlank() || cleanConfirmPass.isBlank()) {
                    Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (cleanNewPass.length < 6) {
                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (cleanNewPass != cleanConfirmPass) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true

                RetrofitClient.instance.resetPassword(email, cleanNewPass)
                    .enqueue(object : Callback<GenericResponse> {

                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {
                            loading = false
                            val res = response.body()

                            if (res?.status == "success") {
                                Toast.makeText(
                                    context,
                                    "Password updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                UserSession.resetEmail = "" // ✅ CLEAR SESSION
                                onResetDone()
                            } else {
                                Toast.makeText(
                                    context,
                                    res?.message ?: "Failed to reset password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            loading = false
                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Reset Password", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
