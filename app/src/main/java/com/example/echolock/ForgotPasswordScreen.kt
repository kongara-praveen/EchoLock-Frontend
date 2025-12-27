package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onVerifySend: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(26.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Forgot Password",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF082B34)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Enter your email to receive verification code.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            enabled = !loading
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            onClick = {

                val cleanEmail = email.trim()

                if (cleanEmail.isBlank()) {
                    Toast.makeText(
                        context,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                loading = true

                RetrofitClient.instance.sendOtp(cleanEmail)
                    .enqueue(object : Callback<GenericResponse> {

                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {
                            loading = false

                            val result = response.body()

                            if (result?.status == "success") {

                                // ✅ SAVE EMAIL FOR NEXT SCREENS
                                UserSession.resetEmail = cleanEmail

                                Toast.makeText(
                                    context,
                                    result.message ?: "OTP sent",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVerifySend()
                            } else {
                                Toast.makeText(
                                    context,
                                    result?.message ?: "Failed to send OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                            loading = false
                            Toast.makeText(
                                context,
                                "Network error: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
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
                Text(
                    text = "Send Verification Code",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
