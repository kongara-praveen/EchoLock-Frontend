package com.example.echolock.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ================= AUTH =================

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("send_otp.php")
    fun sendOtp(
        @Field("email") email: String
    ): Call<GenericResponse>

    // ================= PROFILE =================

    @FormUrlEncoded
    @POST("get_profile.php")
    fun getProfile(
        @Field("email") email: String
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST("update_profile.php")
    fun updateProfile(
        @Field("name") name: String,
        @Field("email") email: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("change_password.php")
    fun changePassword(
        @Field("email") email: String,
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String
    ): Call<GenericResponse>

    // ================= AUDIO MODULE =================

    @Multipart
    @POST("audio/upload_audio.php")
    fun uploadAudio(
        @Part audio: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("audio/get_recent_audio.php")
    fun getRecentAudio(
        @Field("user_id") userId: Int
    ): Call<RecentAudioResponse>

    // ================= IMAGE MODULE =================

    @Multipart
    @POST("image/upload_image.php")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("user_id") userId: RequestBody
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("image/get_recent_images.php")
    fun getRecentImages(
        @Field("user_id") userId: Int
    ): Call<RecentImageResponse>
    // ================= IMAGE STEGANOGRAPHY =================

    @FormUrlEncoded
    @POST("image/hide_message.php")
    fun hideMessage(
        @Field("image_name") imageName: String,
        @Field("message") message: String
    ): Call<StegoResponse>

}
