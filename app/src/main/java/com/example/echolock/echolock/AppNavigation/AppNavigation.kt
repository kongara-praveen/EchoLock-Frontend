// AppNavigation.kt

package com.example.echolock.navigation

import com.example.echolock.session.UserSession
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolock.ui.screens.*

sealed class Screen(val route: String) {

    object Welcome : Screen("welcome")
    object OnBoardAudio : Screen("onboard_audio")
    object OnBoardImage : Screen("onboard_image")
    object OnBoardTamper : Screen("onboard_tamper")

    object UserAccess : Screen("user_access")
    object Login : Screen("login")
    object Signup : Screen("signup")

    object ForgotPassword : Screen("forgot_password")
    object Verification : Screen("verification")
    object ResetPassword : Screen("reset_password")
    object ResetSuccess : Screen("password_reset_success")



    object Terms : Screen("terms_conditions")
    object Privacy : Screen("privacy_policy")

    object Dashboard : Screen("dashboard")

    object Files : Screen("files")
    object History : Screen("history")
    object Settings : Screen("settings")

    // FIXED → only ONE ChangePassword declaration
    object ChangePassword : Screen("change_password")

    object About : Screen("about_app")
    object Faq : Screen("faq")

    // AUDIO ENCRYPT
    object SelectAudio : Screen("select_audio")
    object EncryptAudioMessage : Screen("encrypt_audio_message")
    object EncryptionProgress : Screen("encryption_progress")
    object EncryptionComplete : Screen("encryption_complete")

    // AUDIO DECRYPT
    object DecryptAudio : Screen("decrypt_audio")
    object AudioDecryptionProgress : Screen("audio_decryption_progress")
    object DecryptAudioResult : Screen("decrypt_audio_result")

    // IMAGE ENCRYPT
    object SelectImage : Screen("select_image")
    object EncryptImageMessage : Screen("encrypt_image_message")
    object ImageEncryptionProgress : Screen("image_encryption_progress")
    object ImageEncryptionComplete : Screen("image_encryption_complete")

    // IMAGE DECRYPT
    object DecryptImage : Screen("decrypt_image")
    object ImageDecryptionProgress : Screen("image_decryption_progress")
    object ImageDecryptionResult : Screen("image_decryption_result")

    // LOGOUT
    object Logout : Screen("logout")
    object LogoutSuccess : Screen("logout_success")

    object EditProfile : Screen("edit_profile")
    object PasswordUpdatedSuccess : Screen("password_updated_success")
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {

        /* WELCOME */
        composable(Screen.Welcome.route) {
            WelcomeScreen { navController.navigate(Screen.OnBoardAudio.route) }
        }

        /* ONBOARDING */
        composable(Screen.OnBoardAudio.route) {
            OnBoardingAudioScreen(
                onContinue = { navController.navigate(Screen.OnBoardImage.route) },
                onSkip = { navController.navigate(Screen.UserAccess.route) }
            )
        }

        composable(Screen.OnBoardImage.route) {
            OnBoardingImageScreen(
                onContinue = { navController.navigate(Screen.OnBoardTamper.route) },
                onSkip = { navController.navigate(Screen.UserAccess.route) }
            )
        }

        composable(Screen.OnBoardTamper.route) {
            OnBoardingTamperScreen(
                onContinue = { navController.navigate(Screen.UserAccess.route) },
                onSkip = { navController.navigate(Screen.UserAccess.route) }
            )
        }

        /* AUTH */
        /* AUTH */
        composable(Screen.UserAccess.route) {
            UserAccessScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onCreateAccountClick = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onBack = { navController.popBackStack() },

                // UPDATED : Only navigate after backend success
                onSignIn = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },

                onSignUp = { navController.navigate(Screen.Signup.route) },
                onForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }


        composable(Screen.Signup.route) {
            CreateAccountScreen(
                onBack = { navController.popBackStack() },
                onCreateAccount = { navController.navigate(Screen.Dashboard.route) },
                onSignInClick = { navController.navigate(Screen.Login.route) },
                onTermsClick = { navController.navigate(Screen.Terms.route) },
                onPrivacyClick = { navController.navigate(Screen.Privacy.route) }
            )
        }

        /* RESET PASSWORD FLOW */
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onVerifySend = { navController.navigate(Screen.Verification.route) }
            )
        }

        composable(Screen.Verification.route) {
            VerificationScreen(
                onBack = { navController.popBackStack() },
                onVerify = { navController.navigate(Screen.ResetPassword.route) },
                onResend = {}
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onBack = { navController.popBackStack() },
                onResetDone = { navController.navigate(Screen.ResetSuccess.route) }
            )
        }

        composable(Screen.ResetSuccess.route) {
            PasswordResetSuccessScreen(
                onContinue = { navController.navigate(Screen.Login.route) }
            )
        }

        /* TERMS + PRIVACY */
        composable(Screen.Terms.route) {
            TermsConditionsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Privacy.route) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }



        /* DASHBOARD */
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onEncryptAudio = { navController.navigate(Screen.SelectAudio.route) },
                onEncryptImage = { navController.navigate(Screen.SelectImage.route) },
                onTamperCheck = {},

                onDecryptAudio = { navController.navigate(Screen.DecryptAudio.route) },
                onDecryptImage = { navController.navigate(Screen.DecryptImage.route) },

                onFilesClick = { navController.navigate(Screen.Files.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        /* FILES */
        composable(Screen.Files.route) {
            FilesScreen(
                onBack = { navController.popBackStack() },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        /* HISTORY */
        composable(Screen.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onFilesClick = { navController.navigate(Screen.Files.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        /* SETTINGS */
        /* SETTINGS */
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.navigate(Screen.Dashboard.route) },
                onEditProfileClick = { navController.navigate(Screen.EditProfile.route) },
                onChangePasswordClick = { navController.navigate(Screen.ChangePassword.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onFaqClick = { navController.navigate(Screen.Faq.route) },
                onLogoutClick = { navController.navigate(Screen.Logout.route) },

                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onFilesClick = { navController.navigate(Screen.Files.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onSettingsClick = {}
            )
        }

        /* ABOUT SCREEN */
        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        /* FAQ SCREEN */
        composable(Screen.Faq.route) {
            FAQScreen(
                onBack = { navController.popBackStack() }
            )
        }


        /* AUDIO ENCRYPT */
        composable(Screen.SelectAudio.route) {
            SelectAudioScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Screen.EncryptAudioMessage.route) }
            )
        }

        composable(Screen.EncryptAudioMessage.route) {
            EncryptMessageScreen(
                onBack = { navController.popBackStack() },
                onEncrypt = { navController.navigate(Screen.EncryptionProgress.route) }
            )
        }

        composable(Screen.EncryptionProgress.route) {
            EncryptionProgressScreen(
                onCompleted = { navController.navigate(Screen.EncryptionComplete.route) }
            )
        }

        composable(Screen.EncryptionComplete.route) {
            EncryptionCompleteScreen(
                onDownload = {},
                onBackDashboard = { navController.navigate(Screen.Dashboard.route) }
            )
        }

        /* AUDIO DECRYPT */
        composable(Screen.DecryptAudio.route) {
            DecryptAudioScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Screen.AudioDecryptionProgress.route) }
            )
        }

        composable(Screen.AudioDecryptionProgress.route) {
            AudioDecryptionProgressScreen(
                onCompleted = { navController.navigate(Screen.DecryptAudioResult.route) }
            )
        }

        composable(Screen.DecryptAudioResult.route) {
            DecryptAudioResultScreen(
                onDone = { navController.navigate(Screen.Dashboard.route) }
            )
        }

        /* IMAGE ENCRYPT */
        composable(Screen.SelectImage.route) {
            SelectImageScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Screen.EncryptImageMessage.route) }
            )
        }

        composable(Screen.EncryptImageMessage.route) {

            // 🔥 this must come from upload_image.php response
            val uploadedImageName = UserSession.lastUploadedImageName

            EncryptImageMessageScreen(
                imageName = uploadedImageName,
                onBack = { navController.popBackStack() },
                onSuccess = { stegoFileName ->
                    navController.navigate(
                        Screen.ImageEncryptionProgress.route + "/$stegoFileName"
                    )
                }
            )
        }


        composable(Screen.ImageEncryptionProgress.route) {
            ImageEncryptionProgressScreen(
                onCompleted = { navController.navigate(Screen.ImageEncryptionComplete.route) }
            )
        }

        composable(Screen.ImageEncryptionComplete.route) {
            ImageEncryptionCompleteScreen(
                onDownload = {},
                onBackDashboard = { navController.navigate(Screen.Dashboard.route) }
            )
        }

        /* IMAGE DECRYPT */
        composable(Screen.DecryptImage.route) {
            DecryptImageScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Screen.ImageDecryptionProgress.route) }
            )
        }

        composable(Screen.ImageDecryptionProgress.route) {
            ImageDecryptionProgressScreen(
                onCompleted = { navController.navigate(Screen.ImageDecryptionResult.route) }
            )
        }

        composable(Screen.ImageDecryptionResult.route) {
            DecryptImageResultScreen(
                onDone = { navController.navigate(Screen.Dashboard.route) }
            )
        }

        /* LOGOUT FLOW */
        composable(Screen.Logout.route) {
            LogoutConfirmationScreen(
                onConfirm = { navController.navigate(Screen.LogoutSuccess.route) },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(Screen.LogoutSuccess.route) {
            LogoutSuccessScreen(
                onFinished = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        /* EDIT PROFILE */
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                userEmail = UserSession.email,   // 🔥 PASS LOGGED-IN EMAIL
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }


        /* CHANGE PASSWORD */
        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBack = { navController.popBackStack() },
                onUpdate = { navController.navigate(Screen.PasswordUpdatedSuccess.route) }
            )
        }

        /* PASSWORD UPDATED SUCCESS */
        composable(Screen.PasswordUpdatedSuccess.route) {
            PasswordUpdatedSuccessScreen(
                onFinished = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            )
        }

    }
}
