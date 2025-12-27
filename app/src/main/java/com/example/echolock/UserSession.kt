package com.example.echolock.session

object UserSession {

    /* ================= AUTH / USER ================= */

    var userId: String = ""
    var email: String = ""
    var resetEmail: String = ""

    // 🔐 SERVER ISSUED TOKEN (CRITICAL FOR TAMPER CHECK)
    var serverToken: String = ""

    fun isLoggedIn(): Boolean {
        return serverToken.isNotBlank()
    }

    fun clearUserSession() {
        userId = ""
        email = ""
        serverToken = ""
    }


    /* ================= AUDIO ENCRYPT FLOW ================= */

    // 🎵 STEP 1: Original selected audio
    var originalAudioPath: String? = null
    var originalAudioName: String? = null

    // 🔄 STEP 2: Converted WAV
    var wavAudioPath: String? = null

    // 🔐 STEP 3: Secret message
    var secretMessage: String? = null

    // 🔊 STEP 4: Final stego audio
    var stegoAudioPath: String? = null


    /* ================= AUDIO DECRYPT FLOW ================= */

    var decryptAudioUri: String? = null
    var decryptedMessage: String? = null


    /* ================= IMAGE ENCRYPT / TAMPER ================= */

    // Last encrypted image name
    var lastUploadedImageName: String = ""

    // Optional: store image token if needed later
    var lastImageToken: String? = null


    /* ================= RESET HELPERS ================= */

    fun clearAudioEncryptionSession() {
        originalAudioPath = null
        originalAudioName = null
        wavAudioPath = null
        secretMessage = null
        stegoAudioPath = null
    }

    fun clearAudioDecryptionSession() {
        decryptAudioUri = null
        decryptedMessage = null
    }

    fun clearImageSession() {
        lastUploadedImageName = ""
        lastImageToken = null
    }
}
