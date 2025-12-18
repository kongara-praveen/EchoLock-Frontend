package com.example.echolock.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.echolock.api.RetrofitClient
import com.example.echolock.api.StegoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EncryptImageMessageScreen(
    imageName: String,
    onBack: () -> Unit,
    onSuccess: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Secret Message", fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(22.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFE4FAF0), RoundedCornerShape(10.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(id = R.drawable.ic_lock), "", Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text("Your message will be encrypted safely.")
        }

        Spacer(Modifier.height(22.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = { Text("Type your secret message here...") }
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (message.isBlank()) {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading = true

                RetrofitClient.instance.hideMessage(
                    imageName = imageName,
                    message = message
                ).enqueue(object : Callback<StegoResponse> {

                    override fun onResponse(
                        call: Call<StegoResponse>,
                        response: Response<StegoResponse>
                    ) {
                        loading = false
                        if (response.isSuccessful && response.body()?.status == "success") {
                            onSuccess(response.body()!!.file)
                        } else {
                            Toast.makeText(context, "Encryption failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<StegoResponse>, t: Throwable) {
                        loading = false
                        Log.e("IMAGE_ENCRYPT", t.message ?: "Error")
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()
                    }
                })
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = !loading,
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Encrypt & Hide", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
