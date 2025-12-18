package com.example.echolock.ui.screens

// ===================== REQUIRED IMPORTS =====================
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
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
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        /* ---------- CURRENT PASSWORD ---------- */
        Text("Current Password", fontWeight = FontWeight.Medium)

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
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        /* ---------- NEW PASSWORD ---------- */
        Text("New Password", fontWeight = FontWeight.Medium)

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
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        /* ---------- CONFIRM PASSWORD ---------- */
        Text("Confirm New Password", fontWeight = FontWeight.Medium)

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
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        /* ---------- PASSWORD REQUIREMENTS ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF6FF), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "New Password must contain:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF0F172A)
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
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Update Password", color = Color.White, fontSize = 16.sp)
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
        Text("• ", fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
        Text(text, fontSize = 14.sp, color = Color(0xFF1E3A8A))
    }
}
