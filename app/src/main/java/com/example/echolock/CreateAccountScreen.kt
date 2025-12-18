package com.example.echolock.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.api.RetrofitClient
import com.example.echolock.api.RegisterResponse
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
    /* ---------------- STATE ---------------- */
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isTermsAccepted by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    /* ---------------- ENTRANCE ANIMATION ---------------- */
    val alphaAnim = remember { Animatable(0f) }
    val offsetAnim = remember { Animatable(40f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, tween(700, easing = FastOutSlowInEasing))
        offsetAnim.animateTo(0f, tween(700, easing = FastOutSlowInEasing))
    }

    /* ---------------- BUTTON PRESS ANIMATION ---------------- */
    val buttonScale by animateFloatAsState(
        targetValue = if (isTermsAccepted) 1f else 0.98f,
        animationSpec = tween(300),
        label = "buttonScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp)
            .alpha(alphaAnim.value)
            .offset(y = offsetAnim.value.dp),
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

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            "Create Account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "Join EchoLock for secure steganography.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(modifier = Modifier.height(35.dp))

        /* ---------------- INPUT FIELDS ---------------- */

        AnimatedField {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        AnimatedField {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        AnimatedField {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector =
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF005F73),
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }
                    )
                }
            )
        }

        AnimatedField {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation =
                    if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector =
                            if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF005F73),
                        modifier = Modifier.clickable {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------------- TERMS ---------------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it }
            )

            Text("I agree to the ", fontSize = 14.sp, color = Color(0xFF6B7E80))
            Text(
                "Terms of Service",
                color = Color(0xFF005F73),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onTermsClick() }
            )
            Text(" and ", fontSize = 14.sp, color = Color(0xFF6B7E80))
            Text(
                "Privacy Policy",
                color = Color(0xFF005F73),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onPrivacyClick() }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        /* ---------------- CREATE ACCOUNT BUTTON ---------------- */
        Button(
            onClick = {
                if (fullName.isBlank() || email.isBlank() ||
                    password.isBlank() || confirmPassword.isBlank()
                ) {
                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                RetrofitClient.instance.register(
                    fullName,
                    email,
                    password,
                    confirmPassword
                ).enqueue(object : Callback<RegisterResponse> {

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        val result = response.body()
                        if (result?.status == "success") {
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            onCreateAccount()
                        } else {
                            Toast.makeText(context, result?.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            enabled = isTermsAccepted,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .scale(buttonScale),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Create Account", color = Color.White, fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account?", color = Color(0xFF6B7E80))
            Spacer(Modifier.width(6.dp))
            Text(
                "Sign In",
                color = Color(0xFF005F73),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onSignInClick() }
            )
        }
    }
}

/* ---------------- REUSABLE FIELD ANIMATION ---------------- */
@Composable
private fun AnimatedField(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(500)
        )
    ) {
        Column {
            content()
            Spacer(Modifier.height(14.dp))
        }
    }
}
