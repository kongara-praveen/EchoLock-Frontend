package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.LoginResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Animation for screen entrance
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
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() },
            tint = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Sign in to access your secure dashboard.",
            fontSize = 15.sp,
            color = AppColors.TextSecondary,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // EMAIL
        Text(
            "Email Address",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    "name@gmail.com",
                    color = AppColors.TextTertiary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Normal
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.PrimaryDark,
                unfocusedBorderColor = AppColors.BorderLight,
                cursorColor = AppColors.PrimaryDark,
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedPlaceholderColor = AppColors.TextTertiary,
                unfocusedPlaceholderColor = AppColors.TextTertiary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // PASSWORD
        Text(
            "Password",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    "••••••••",
                    color = AppColors.TextTertiary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = AppColors.TextSecondary
                    )
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Normal
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.PrimaryDark,
                unfocusedBorderColor = AppColors.BorderLight,
                cursorColor = AppColors.PrimaryDark,
                focusedTextColor = AppColors.TextPrimary,
                unfocusedTextColor = AppColors.TextPrimary,
                focusedPlaceholderColor = AppColors.TextTertiary,
                unfocusedPlaceholderColor = AppColors.TextTertiary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Forgot Password?",
            color = AppColors.PrimaryDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPassword() }
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        // LOGIN BUTTON
        val buttonEnabled by remember { derivedStateOf { email.isNotBlank() && password.isNotBlank() && !isLoading } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )
        
        Button(
            onClick = {
                val cleanEmail = email.trim()
                val cleanPassword = password.trim()

                if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true
                RetrofitClient.instance.login(cleanEmail, cleanPassword)
                    .enqueue(object : Callback<LoginResponse> {

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            isLoading = false
                            val result = response.body()

                            if (response.isSuccessful && result?.status == "success") {

                                // 🔥 SAVE SESSION EMAIL AND USER ID
                                UserSession.email = cleanEmail
                                result.user_id?.let { userId ->
                                    UserSession.userId = userId.toString()
                                }

                                Toast.makeText(
                                    context,
                                    result.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                onSignIn()
                            } else {
                                Toast.makeText(
                                    context,
                                    result?.message ?: "Login failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Network error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryDark,
                disabledContainerColor = AppColors.BorderLight
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Log In", fontSize = 17.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ", color = AppColors.TextSecondary, fontSize = 14.sp)
            Text(
                "Sign Up",
                color = AppColors.PrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSignUp() }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
