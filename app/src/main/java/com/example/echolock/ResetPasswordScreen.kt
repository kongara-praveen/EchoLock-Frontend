package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.theme.AppColors
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

    var newPass by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }

    var newPassVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassVisible by rememberSaveable { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val email = UserSession.resetEmail.trim() // ✅ REQUIRED

    // Screen entrance animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
            color = AppColors.TextPrimary
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Create a new strong password for your account.",
            fontSize = 15.sp,
            color = AppColors.TextSecondary
        )

        Spacer(Modifier.height(28.dp))

        // 🔐 NEW PASSWORD
        OutlinedTextField(
            value = newPass,
            onValueChange = { newPass = it },
            label = { Text("New Password", color = AppColors.TextTertiary) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            visualTransformation =
                if (newPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Normal
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedBorderColor = AppColors.PrimaryDark,
                unfocusedBorderColor = AppColors.BorderLight,
                cursorColor = AppColors.PrimaryDark,
                focusedPlaceholderColor = AppColors.TextTertiary,
                unfocusedPlaceholderColor = AppColors.TextTertiary
            ),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (newPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = AppColors.PrimaryDark,
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
            label = { Text("Confirm New Password", color = AppColors.TextTertiary) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            visualTransformation =
                if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Normal
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedBorderColor = AppColors.PrimaryDark,
                unfocusedBorderColor = AppColors.BorderLight,
                cursorColor = AppColors.PrimaryDark,
                focusedPlaceholderColor = AppColors.TextTertiary,
                unfocusedPlaceholderColor = AppColors.TextTertiary
            ),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (confirmPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = AppColors.PrimaryDark,
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
                    color = AppColors.TextOnPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Reset Password", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
