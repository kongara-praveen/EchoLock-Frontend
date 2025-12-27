package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun VerificationScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit
) {

    var otp by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val email = UserSession.resetEmail.trim()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

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

        Spacer(Modifier.height(25.dp))

        Text(
            "Verification",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF082B34)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Enter the 6-digit code sent to your email.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(Modifier.height(30.dp))

        // Hidden OTP input
        OutlinedTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all(Char::isDigit)) {
                    otp = it
                }
            },
            modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent
            )
        )

        // OTP boxes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(6) { index ->
                OtpBox(otp.getOrNull(index)?.toString() ?: "")
            }
        }

        Spacer(Modifier.height(30.dp))

        // VERIFY BUTTON
        Button(
            enabled = otp.length == 6 && !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            onClick = {

                if (email.isBlank()) {
                    Toast.makeText(context, "Email missing. Go back.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true

                RetrofitClient.instance.verifyOtp(email, otp)
                    .enqueue(object : Callback<GenericResponse> {

                        override fun onResponse(
                            call: Call<GenericResponse>,
                            response: Response<GenericResponse>
                        ) {
                            loading = false
                            val res = response.body()

                            if (res?.status == "success") {
                                Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show()
                                onVerified()
                            } else {
                                Toast.makeText(
                                    context,
                                    res?.message ?: "Invalid OTP",
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
                Text("Verify Code", color = Color.White, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // RESEND OTP (✅ AUTO CLEAR OTP)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Didn't receive code?")
            Spacer(Modifier.width(6.dp))
            Text(
                "Resend",
                color = Color(0xFF005F73),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = !loading) {

                    if (email.isBlank()) {
                        Toast.makeText(context, "Email missing", Toast.LENGTH_SHORT).show()
                        return@clickable
                    }

                    // ✅ CLEAR PREVIOUS OTP
                    otp = ""
                    focusRequester.requestFocus()
                    keyboardController?.show()

                    loading = true
                    RetrofitClient.instance.sendOtp(email)
                        .enqueue(object : Callback<GenericResponse> {

                            override fun onResponse(
                                call: Call<GenericResponse>,
                                response: Response<GenericResponse>
                            ) {
                                loading = false
                                Toast.makeText(context, "OTP sent again", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                                loading = false
                                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            )
        }
    }
}
@Composable
fun OtpBox(value: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.5.dp,
                color = Color(0xFF005F73),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

