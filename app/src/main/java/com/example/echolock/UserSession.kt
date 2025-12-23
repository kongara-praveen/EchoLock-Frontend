package com.example.echolock.session

object UserSession {

    /* ---------------- USER ---------------- */
    var email: String = ""

    /* ================= AUDIO ENCRYPT FLOW ================= */

    // 🎵 STEP 1: Original selected audio
    var originalAudioPath: String? = null
    var originalAudioName: String? = null

    // 🔄 STEP 2: Converted WAV (after AudioConversionScreen)
    var wavAudioPath: String? = null

    // 🔐 STEP 3: Secret message entered by user
    var secretMessage: String? = null

    // 🔊 STEP 4: Final encrypted / stego audio
    var stegoAudioPath: String? = null


    /* ================= AUDIO DECRYPT FLOW ================= */

    var decryptAudioUri: String? = null
    var decryptedMessage: String? = null


    /* ================= IMAGE (OPTIONAL) ================= */

    var lastUploadedImageName: String = ""


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
}
