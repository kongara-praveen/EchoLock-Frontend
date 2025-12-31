package com.example.echolock.ui.screens

// ===================== REQUIRED IMPORTS =====================
import androidx.compose.animation.core.*
import androidx.compose.ui.text.TextStyle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.GenericResponse
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
// ===========================================================

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onUpdate: () -> Unit
) {

    val context = LocalContext.current

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                "Change Password",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        /* ---------- CURRENT PASSWORD ---------- */
        Text("Current Password", fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)

        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (currentVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (currentVisible) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        currentVisible = !currentVisible
                    }
                )
            },
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
            )
        )


        Spacer(modifier = Modifier.height(18.dp))

        /* ---------- NEW PASSWORD ---------- */
        Text("New Password", fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (newVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (newVisible) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        newVisible = !newVisible
                    }
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF0A2E45)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0A2E45),
                unfocusedTextColor = Color(0xFF0A2E45),
                focusedBorderColor = Color(0xFF005F73),
                unfocusedBorderColor = Color(0xFFD0DBDF),
                cursorColor = Color(0xFF005F73)
            )
        )


        Spacer(modifier = Modifier.height(18.dp))

        /* ---------- CONFIRM PASSWORD ---------- */
        Text("Confirm New Password", fontWeight = FontWeight.Medium, color = AppColors.TextPrimary)

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (confirmVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector =
                        if (confirmVisible) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        confirmVisible = !confirmVisible
                    }
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF0A2E45)
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0A2E45),
                unfocusedTextColor = Color(0xFF0A2E45),
                focusedBorderColor = Color(0xFF005F73),
                unfocusedBorderColor = Color(0xFFD0DBDF),
                cursorColor = Color(0xFF005F73)
            )
        )


        Spacer(modifier = Modifier.height(22.dp))

        /* ---------- PASSWORD REQUIREMENTS ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.Info.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "New Password must contain:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                RequirementItem("At least 8 characters")
                RequirementItem("One uppercase letter (A–Z)")
                RequirementItem("One lowercase letter (a–z)")
                RequirementItem("One number (0–9)")
                RequirementItem("One special character (!@#\$%^&*)")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        /* ---------- UPDATE BUTTON ---------- */
        Button(
            onClick = {

                if (currentPassword.isBlank() ||
                    newPassword.isBlank() ||
                    confirmPassword.isBlank()
                ) {
                    Toast.makeText(
                        context,
                        "All fields are required",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (newPassword != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                RetrofitClient.instance.changePassword(
                    email = UserSession.email,
                    currentPassword = currentPassword,
                    newPassword = newPassword
                ).enqueue(object : Callback<GenericResponse> {

                    override fun onResponse(
                        call: Call<GenericResponse>,
                        response: Response<GenericResponse>
                    ) {
                        val result = response.body()
                        if (result?.status == "success") {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            onUpdate()
                        } else {
                            Toast.makeText(
                                context,
                                result?.message ?: "Update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
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
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text("Update Password", color = AppColors.TextOnPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/* ---------- REQUIREMENT ITEM ---------- */
@Composable
fun RequirementItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text("• ", fontWeight = FontWeight.Bold, color = AppColors.Info)
        Text(text, fontSize = 14.sp, color = AppColors.Info)
    }
}
