package com.example.echolock.session

object UserSession {

    /* ================= AUTH / USER ================= */

    var userId: String = ""
    var email: String = ""
    var resetEmail: String = ""
    var name: String = ""
    var profileImage: String = ""

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
    var selectedAudioUriString: String? = null  // For UI state persistence

    // 🔄 STEP 2: Converted WAV
    var wavAudioPath: String? = null

    // 🔐 STEP 3: Secret message
    var secretMessage: String? = null
    
    // 🔑 STEP 3.5: Encryption password
    var encryptionPassword: String? = null

    // 🔊 STEP 4: Final stego audio
    var stegoAudioPath: String? = null


    /* ================= AUDIO DECRYPT FLOW ================= */

    var decryptAudioUri: String? = null
    var decryptAudioUriString: String? = null  // For UI state persistence
    var decryptedMessage: String? = null
    var decryptionPassword: String? = null


    /* ================= IMAGE ENCRYPT / TAMPER ================= */

    // Last encrypted image name
    var lastUploadedImageName: String = ""

    // Optional: store image token if needed later
    var lastImageToken: String? = null
    
    // UI state persistence
    var selectedImageUriString: String? = null  // For SelectImageScreen
    var encryptImageMessage: String? = null  // For EncryptImageMessageScreen
    var encryptImagePassword: String? = null  // For EncryptImageMessageScreen
    var decryptImageUriString: String? = null  // For DecryptImageScreen
    var decryptImagePassword: String? = null  // For DecryptImageScreen

    /* ================= FILES SCREEN ================= */
    
    var selectedFile: com.example.echolock.model.FileItem? = null


    /* ================= RESET HELPERS ================= */

    fun clearAudioEncryptionSession() {
        originalAudioPath = null
        originalAudioName = null
        selectedAudioUriString = null
        wavAudioPath = null
        secretMessage = null
        encryptionPassword = null
        stegoAudioPath = null
    }

    fun clearAudioDecryptionSession() {
        decryptAudioUri = null
        decryptAudioUriString = null
        decryptedMessage = null
        decryptionPassword = null
    }

    fun clearImageSession() {
        lastUploadedImageName = ""
        lastImageToken = null
        selectedImageUriString = null
        encryptImageMessage = null
        encryptImagePassword = null
        decryptImageUriString = null
        decryptImagePassword = null
    }
}
