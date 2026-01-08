package com.example.echolock.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Query


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

    @FormUrlEncoded
    @POST("verify_otp.php")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<GenericResponse>


    // ================= PROFILE =================

    @FormUrlEncoded
    @POST("get_profile.php")
    fun getProfile(
        @Field("email") email: String
    ): Call<ProfileResponse>


    @FormUrlEncoded
    @POST("verify_password.php")
    fun verifyPassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("update_profile.php")
    fun updateProfile(
        @Field("old_email") oldEmail: String,
        @Field("email") email: String,
        @Field("full_name") fullName: String
    ): Call<GenericResponse>



    @Multipart
    @POST("upload_profile_image.php")
    fun uploadProfileImage(
        @Part image: MultipartBody.Part,
        @Part("email") email: RequestBody
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
        @Field("image_name") imageName: String,   // ✅ MUST MATCH PHP
        @Field("message") message: String          // ✅ MUST MATCH PHP
    ): Call<StegoResponse>
    // ================= FILES =================
    
    @GET("files/get_files.php")
    suspend fun getFiles(
        @Query("user_id") userId: Int
    ): FilesResponse
    
    @FormUrlEncoded
    @POST("files/delete_file.php")
    suspend fun deleteFile(
        @Field("file_id") fileId: Int,
        @Field("file_type") fileType: String
    ): GenericResponse
    @GET("history/get_history.php")
    suspend fun getHistory(
        @Query("user_id") userId: Int
    ): HistoryResponse
    @FormUrlEncoded
    @POST("history/add_history.php")
    suspend fun addHistory(
        @Field("user_id") userId: Int,
        @Field("file_name") fileName: String,
        @Field("action") action: String
    ): GenericResponse
    @FormUrlEncoded
    @POST("tamper/register_encryption.php")
    fun createImageToken(
        @Field("file_name") fileName: String
    ): Call<TokenResponse>
    
    // ================= INTEGRITY SIGNATURE =================
    
    @FormUrlEncoded
    @POST("tamper/store_signature.php")
    suspend fun storeIntegritySignature(
        @Field("file_name") fileName: String,
        @Field("user_id") userId: Int,
        @Field("image_hash") imageHash: String,
        @Field("integrity_signature") integritySignature: String
    ): GenericResponse
    
    @FormUrlEncoded
    @POST("tamper/get_signature.php")
    suspend fun getIntegritySignature(
        @Field("file_name") fileName: String,
        @Field("user_id") userId: Int
    ): SignatureResponse
    @FormUrlEncoded
    @POST("reset_password.php")
    fun resetPassword(
        @Field("email") email: String,
        @Field("password") password: String   // ✅ MUST BE "password"
    ): Call<GenericResponse>
    @FormUrlEncoded
    @POST("signup_send_otp.php")
    fun signupSendOtp(
        @Field("email") email: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("signup_verify_otp.php")
    fun signupVerifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<GenericResponse>



}
