package com.example.echolock.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

object TamperCheckUtil {

    private const val VERIFY_URL =
        "http://10.0.2.2/echolock_api/tamper/verify_encryption.php"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    /**
     * TRUE  → SAFE
     * FALSE → TAMPERED / INVALID
     */
    suspend fun checkFile(file: File): Boolean =
        withContext(Dispatchers.IO) {

            // 1️⃣ Decode image
            val bitmap: Bitmap =
                BitmapFactory.decodeFile(file.absolutePath)
                    ?: return@withContext false

            // 2️⃣ Decode stego payload
            val decoded =
                ImageSteganography.decode(bitmap)
                    ?: return@withContext false

            // 3️⃣ Send token to server
            val body = FormBody.Builder()
                .add("token", decoded.token)
                .build()

            val request = Request.Builder()
                .url(VERIFY_URL)
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                val jsonString = response.body?.string()
                    ?: return@withContext false

                val json = JSONObject(jsonString)

                // 4️⃣ STRICT JSON check
                return@withContext json.optBoolean("safe", false)

            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }
}
