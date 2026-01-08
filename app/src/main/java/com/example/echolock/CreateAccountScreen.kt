package com.example.echolock.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RegisterResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.ui.theme.GradientBackgrounds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onSignInClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    /* ---------- STATE ---------- */
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var otp by rememberSaveable { mutableStateOf("") }
    var otpSent by rememberSaveable { mutableStateOf(false) }
    var otpVerified by rememberSaveable { mutableStateOf(false) }

    var isTermsAccepted by rememberSaveable { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() },
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Create Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Join EchoLock securely",
                    fontSize = 14.sp,
                    color = Color(0xFFBEE7E8)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- COMMON FIELD COLORS ---------- */
        val fieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF38BDF8),
            unfocusedBorderColor = Color(0xFF475569),
            cursorColor = Color(0xFF38BDF8),
            focusedPlaceholderColor = Color(0xFF94A3B8),
            unfocusedPlaceholderColor = Color(0xFF94A3B8),
            focusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.6f),
            unfocusedContainerColor = Color(0xFF1E293B).copy(alpha = 0.4f)
        )

        val fieldTextStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )

        /* ---------- INPUTS ---------- */

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name", color = Color(0xFFBEE7E8)) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = fieldTextStyle,
            colors = fieldColors,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", color = Color(0xFFBEE7E8)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !otpSent,
            textStyle = fieldTextStyle,
            colors = fieldColors,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color(0xFFBEE7E8)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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
                        tint = Color(0xFFBEE7E8)
                    )
                }
            },
            textStyle = fieldTextStyle,
            colors = fieldColors,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = Color(0xFFBEE7E8)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation =
                if (confirmPasswordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible)
                            Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = Color(0xFFBEE7E8)
                    )
                }
            },
            textStyle = fieldTextStyle,
            colors = fieldColors,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(20.dp))

        /* ---------- SEND OTP ---------- */
        AnimatedVisibility(
            visible = !otpSent,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Button(
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38BDF8)
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 2.dp
                ),
                onClick = {
                    if (email.isBlank()) {
                        Toast.makeText(context, "Enter email first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true
                    RetrofitClient.instance.signupSendOtp(email)
                        .enqueue(object : Callback<GenericResponse> {
                            override fun onResponse(
                                call: Call<GenericResponse>,
                                response: Response<GenericResponse>
                            ) {
                                loading = false
                                if (response.body()?.status == "success") {
                                    otpSent = true
                                    otp = ""
                                    Toast.makeText(context, "OTP sent", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        response.body()?.message ?: "OTP failed",
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
                Text("Send OTP", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        /* ---------- OTP VERIFY ---------- */
        AnimatedVisibility(
            visible = otpSent && !otpVerified,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    label = { Text("Enter 6-digit OTP", color = AppColors.TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = fieldTextStyle,
                    colors = fieldColors,
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    enabled = !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryDark
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    onClick = {
                        if (otp.length != 6) {
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        loading = true
                        RetrofitClient.instance.signupVerifyOtp(email, otp)
                            .enqueue(object : Callback<GenericResponse> {
                                override fun onResponse(
                                    call: Call<GenericResponse>,
                                    response: Response<GenericResponse>
                                ) {
                                    loading = false
                                    if (response.body()?.status == "success") {
                                        otpVerified = true
                                        Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                    loading = false
                                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                ) {
                    Text("Verify OTP", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        /* ---------- TERMS ---------- */
        AnimatedVisibility(
            visible = otpVerified,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isTermsAccepted,
                        onCheckedChange = { isTermsAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AppColors.PrimaryDark
                        )
                    )

                    Text("I agree to ", color = Color(0xFFBEE7E8), fontSize = 14.sp)
                    Text(
                        "Terms",
                        color = Color(0xFF38BDF8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onTermsClick() }
                    )
                    Text(" and ", color = Color(0xFFBEE7E8), fontSize = 14.sp)
                    Text(
                        "Privacy Policy",
                        color = Color(0xFF38BDF8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onPrivacyClick() }
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        /* ---------- CREATE ACCOUNT ---------- */
        val buttonEnabled by remember { derivedStateOf { otpVerified && isTermsAccepted && !loading } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF38BDF8),
                disabledContainerColor = Color(0xFF475569)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            onClick = {

                if (fullName.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                RetrofitClient.instance.register(
                    fullName, email, password, confirmPassword
                ).enqueue(object : Callback<RegisterResponse> {

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        val result = response.body()
                        if (result?.status == "success") {
                            // Save user session data
                            UserSession.email = email
                            result.user_id?.let { userId ->
                                UserSession.userId = userId.toString()
                            }
                            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                            onCreateAccount()
                        } else {
                            Toast.makeText(
                                context,
                                response.body()?.message ?: "Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        ) {
            Text("Create Account", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already have an account? ", color = Color(0xFFBEE7E8), fontSize = 14.sp)
            Text(
                "Sign In",
                color = Color(0xFF38BDF8),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSignInClick() }
            )
        }
        Text(
            text = "2026 © Powered  by SIMATS Engineering",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}