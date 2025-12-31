// AppNavigation.kt

package com.example.echolock.navigation
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.echolock.session.UserSession
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolock.ui.screens.*
import android.net.Uri
import java.io.File
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.echolock.ui.theme.EchoLockTheme
// 🔥 SIMPLE GLOBAL THEME STATE
enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

val currentAppTheme = mutableStateOf(AppTheme.SYSTEM)


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
    object EncryptImageMessage : Screen("encrypt_image_message/{imageName}")
    object ImageEncryptionProgress : Screen("image_encryption_progress")
    object ImageEncryptionComplete :
        Screen("image_encryption_complete/{imageName}")

    // IMAGE DECRYPT
    // IMAGE DECRYPT
    object DecryptImage : Screen("decrypt_image")
    object ImageDecryptionProgress : Screen("image_decryption_progress/{imageUri}")
    object ImageDecryptionResult :
        Screen("image_decryption_result/{imageName}/{message}")

    object AudioConversion : Screen("audio_conversion")
    // LOGOUT
    object Logout : Screen("logout")
    object LogoutSuccess : Screen("logout_success")

    object EditProfile : Screen("edit_profile")
    object PasswordUpdatedSuccess : Screen("password_updated_success")
    // TAMPER CHECK
    object TamperUpload : Screen("tamper_upload")
    object TamperProgress : Screen("tamper_progress/{fileUri}/{fileName}")
    object TamperResult : Screen("tamper_result/{isSafe}/{fileName}/{fileUri}")
    object AppTheme : Screen("app_theme")
    object FileStorage : Screen("file_storage")
    object Notifications : Screen("notifications")
    
    // FILE DETAILS
    object AudioFileDetail : Screen("audio_file_detail")
    object ImageFileDetail : Screen("image_file_detail")

}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val theme = currentAppTheme.value

    EchoLockTheme(
        darkTheme = when (theme) {
            AppTheme.DARK -> true
            AppTheme.LIGHT -> false
            AppTheme.SYSTEM -> isSystemInDarkTheme()
        }
    ) {

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
                onVerified = { navController.navigate(Screen.ResetPassword.route) }
            )

        }


        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onBack = { navController.popBackStack() },
                onResetDone = { navController.navigate(Screen.ResetSuccess.route) }
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onBack = { navController.popBackStack() },
                onResetDone = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
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
                onTamperCheck = {
                    navController.navigate(Screen.TamperUpload.route)
                },


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
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onFileClick = { file ->
                    UserSession.selectedFile = file
                    if (file.type == "audio") {
                        navController.navigate(Screen.AudioFileDetail.route)
                    } else {
                        navController.navigate(Screen.ImageFileDetail.route)
                    }
                }
            )
        }
        
        /* FILE DETAILS */
        composable(Screen.AudioFileDetail.route) {
            UserSession.selectedFile?.let { file ->
                AudioFileDetailScreen(
                    file = file,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        // Delete will be handled by FilesViewModel
                        navController.popBackStack()
                    }
                )
            }
        }
        
        composable(Screen.ImageFileDetail.route) {
            UserSession.selectedFile?.let { file ->
                ImageFileDetailScreen(
                    file = file,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        // Delete will be handled by FilesViewModel
                        navController.popBackStack()
                    }
                )
            }
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
                onEditProfileClick = {
                    if (UserSession.email.isNotBlank()) {
                        navController.navigate(Screen.EditProfile.route)
                    }
                },

                        onChangePasswordClick = { navController.navigate(Screen.ChangePassword.route) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onFaqClick = { navController.navigate(Screen.Faq.route) },
                onLogoutClick = { navController.navigate(Screen.Logout.route) },
                onAppThemeClick = { navController.navigate(Screen.AppTheme.route) },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onFilesClick = { navController.navigate(Screen.Files.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onFileStorageClick = { navController.navigate(Screen.FileStorage.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) },

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
                onGoEncrypt = {
                    navController.navigate(Screen.EncryptAudioMessage.route)
                },
                onGoConvert = {
                    navController.navigate(Screen.AudioConversion.route)
                }
            )

        }

        composable(Screen.EncryptAudioMessage.route) {
            EncryptMessageScreen(
                onBack = { navController.popBackStack() },
                onEncrypt = { navController.navigate(Screen.EncryptionProgress.route) }
            )
        }
        composable(Screen.AudioConversion.route) {
            AudioConversionScreen(
                onSuccess = {
                    navController.navigate(Screen.EncryptAudioMessage.route) {
                        popUpTo(Screen.SelectAudio.route) { inclusive = false }
                    }
                },
                onFailed = {
                    navController.popBackStack()
                }
            )
        }


        composable(Screen.EncryptionProgress.route) {
            EncryptionProgressScreen(
                onCompleted = {
                    navController.navigate(Screen.EncryptionComplete.route)
                }
            )
        }


        composable(Screen.EncryptionComplete.route) {
            EncryptionCompleteScreen(
                onBackDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
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
                onContinue = { imageName ->
                    navController.navigate(
                        "encrypt_image_message/${Uri.encode(imageName)}"
                    )

                }

            )
        }

        composable(
            route = Screen.EncryptImageMessage.route,
            arguments = listOf(
                navArgument("imageName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val imageName =
                backStackEntry.arguments?.getString("imageName") ?: ""

            EncryptImageMessageScreen(
                imageUri = imageName,   // ✅ PASS AS imageUri
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.ImageEncryptionProgress.route)
                }
            )

        }




        composable(Screen.ImageEncryptionProgress.route) {
            ImageEncryptionProgressScreen(
                onCompleted = { navController.navigate(Screen.ImageEncryptionComplete.route) }
            )
        }

        composable(
            route = Screen.ImageEncryptionComplete.route,
            arguments = listOf(
                navArgument("imageName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val imageName =
                backStackEntry.arguments?.getString("imageName") ?: ""

            ImageEncryptionCompleteScreen(
                onBackDashboard = {
                    navController.navigate("dashboard")
                }
            )

        }


        /* IMAGE DECRYPT */
        composable(Screen.DecryptImage.route) {
            DecryptImageScreen(
                onBack = { navController.popBackStack() },
                onContinue = { uri ->
                    navController.navigate("image_decryption_progress/${Uri.encode(uri)}")
                }
            )

        }

        composable(
            route = Screen.ImageDecryptionProgress.route,
            arguments = listOf(
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val imageUri =
                backStackEntry.arguments?.getString("imageUri") ?: ""

            ImageDecryptionProgressScreen(
                imageUri = imageUri,
                onCompleted = { imageName, extractedMessage ->
                    navController.navigate(
                        "image_decryption_result/${Uri.encode(imageName)}/${
                            Uri.encode(
                                extractedMessage
                            )
                        }"
                    )
                },
                onFailed = {
                    navController.popBackStack() // 👈 Go back if no message
                }
            )

        }


        composable(
            route = Screen.ImageDecryptionResult.route,
            arguments = listOf(
                navArgument("imageName") { type = NavType.StringType },
                navArgument("message") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val imageName =
                backStackEntry.arguments?.getString("imageName") ?: ""

            val extractedMessage =
                backStackEntry.arguments?.getString("message") ?: ""

            DecryptImageResultScreen(
                imageName = imageName,
                extractedMessage = extractedMessage,
                onBack = { navController.popBackStack() },
                onDone = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
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
                onBack = { navController.popBackStack() },

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
        /* ================== TAMPER CHECK ================== */

        composable(Screen.TamperUpload.route) {
            TamperCheckScreen(
                onBack = { navController.popBackStack() },
                onStartCheck = { uri, fileName ->
                    navController.navigate(
                        "tamper_progress/${Uri.encode(uri.toString())}/${Uri.encode(fileName ?: "")}"
                    )
                }
            )
        }


        composable(
            route = Screen.TamperProgress.route,
            arguments = listOf(
                navArgument("fileUri") { type = NavType.StringType },
                navArgument("fileName") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val uri = Uri.parse(
                backStackEntry.arguments!!.getString("fileUri")!!
            )
            val fileName = backStackEntry.arguments!!.getString("fileName") ?: ""

            TamperCheckProgressScreen(
                fileUri = uri,
                originalFileName = if (fileName.isBlank()) null else fileName,
                onResult = { isSafe, resultFileName ->
                    navController.navigate(
                        "tamper_result/$isSafe/${Uri.encode(resultFileName)}/${Uri.encode(uri.toString())}"
                    ) {
                        // 🔥 THIS PREVENTS APP CLOSE
                        popUpTo(Screen.TamperProgress.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }




        composable(
            route = Screen.TamperResult.route,
            arguments = listOf(
                navArgument("isSafe") { type = NavType.BoolType },
                navArgument("fileName") { type = NavType.StringType },
                navArgument("fileUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val fileUriString = backStackEntry.arguments!!.getString("fileUri")!!
            val fileUri = Uri.parse(fileUriString)
            val originalFileName = backStackEntry.arguments!!.getString("fileName")!!

            TamperCheckCompleteScreen(
                isSafe = backStackEntry.arguments!!.getBoolean("isSafe"),
                fileName = originalFileName,
                onBackDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onCheckAgain = {
                    // Navigate directly to progress screen with the same file
                    navController.navigate(
                        "tamper_progress/${Uri.encode(fileUri.toString())}/${Uri.encode(originalFileName)}"
                    ) {
                        popUpTo(Screen.TamperResult.route) { inclusive = true }
                    }
                }
            )

        }
        composable(Screen.AppTheme.route) {
            AppThemeScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.FileStorage.route) {
            FileStorageScreen { navController.popBackStack() }
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen { navController.popBackStack() }
        }


    }
    }
}
