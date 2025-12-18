package com.example.echolock.ui.screens

import android.widget.Toast
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

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(22.dp))

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Welcome Back",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A2E45)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "Sign in to access your secure dashboard.",
            fontSize = 15.sp,
            color = Color(0xFF6A7A7F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // EMAIL
        Text("Email Address", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("name@agency.gov") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(fontSize = 15.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF7FAFB),
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        // PASSWORD
        Text("Password", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        passwordVisible = !passwordVisible
                    }
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF7FAFB),
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Forgot Password?",
            color = Color(0xFF005F73),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPassword() }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // LOGIN BUTTON
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

                RetrofitClient.instance.login(cleanEmail, cleanPassword)
                    .enqueue(object : Callback<LoginResponse> {

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            val result = response.body()

                            if (response.isSuccessful && result?.status == "success") {

                                // 🔥 SAVE SESSION EMAIL
                                UserSession.email = cleanEmail

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
                            Toast.makeText(
                                context,
                                "Network error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Log In", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don’t have an account? ", color = Color.Gray)
            Text(
                "Sign Up",
                color = Color(0xFF005F73),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSignUp() }
            )
        }
    }
}
