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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RegisterResponse
import com.example.echolock.api.RetrofitClient
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

    /* ---------- COLORS ---------- */
    val inputTextColor = Color(0xFF0A2E45)
    val borderColor = Color(0xFFD0DBDF)
    val primaryColor = Color(0xFF005F73)

    /* ---------- STATE ---------- */
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpVerified by remember { mutableStateOf(false) }

    var isTermsAccepted by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    /* ---------- BACKGROUND ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF7FBFD),
                        Color(0xFFEFF6F8)
                    )
                )
            )
    ) {

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
                    .clickable { onBack() }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Create Account",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = inputTextColor
            )

            Text(
                "Join EchoLock securely",
                color = Color(0xFF6B7E80)
            )

            Spacer(Modifier.height(25.dp))

            /* ---------- COMMON FIELD COLORS ---------- */
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = inputTextColor,
                unfocusedTextColor = inputTextColor,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = borderColor,
                cursorColor = primaryColor
            )

            val fieldTextStyle = TextStyle(
                fontSize = 15.sp,
                color = inputTextColor
            )

            /* ---------- INPUTS ---------- */

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = fieldTextStyle,
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !otpSent,
                textStyle = fieldTextStyle,
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }
                    )
                },
                textStyle = fieldTextStyle,
                colors = fieldColors
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (confirmPasswordVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmPasswordVisible)
                            Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    )
                },
                textStyle = fieldTextStyle,
                colors = fieldColors
            )

            Spacer(Modifier.height(18.dp))

            /* ---------- SEND OTP ---------- */
            if (!otpSent) {
                Button(
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(primaryColor),
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
                    Text("Send OTP", color = Color.White)
                }
            }

            /* ---------- OTP VERIFY ---------- */
            if (otpSent && !otpVerified) {

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    label = { Text("Enter 6-digit OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = fieldTextStyle,
                    colors = fieldColors
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(primaryColor),
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
                    Text("Verify OTP", color = Color.White)
                }
            }

            /* ---------- TERMS ---------- */
            if (otpVerified) {

                Spacer(Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isTermsAccepted,
                        onCheckedChange = { isTermsAccepted = it }
                    )

                    Text("I agree to ")
                    Text(
                        "Terms",
                        color = primaryColor,
                        modifier = Modifier.clickable { onTermsClick() }
                    )
                    Text(" and ")
                    Text(
                        "Privacy Policy",
                        color = primaryColor,
                        modifier = Modifier.clickable { onPrivacyClick() }
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            /* ---------- CREATE ACCOUNT ---------- */
            Button(
                enabled = otpVerified && isTermsAccepted && !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(primaryColor),
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
                            if (response.body()?.status == "success") {
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
                Text("Create Account", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ")
                Text(
                    "Sign In",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignInClick() }
                )
            }
        }
    }
}
