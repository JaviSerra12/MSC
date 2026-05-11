package com.example.msc.ui.screen.profileScreen

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.msc.data.repository.FirebaseAuthRepository
import com.example.msc.domain.usecase.auth.*
import com.example.msc.ui.components.profile.ProfileContent
import com.example.msc.ui.navigation.RouteGeneral

@Composable
fun ProfileScreen(navController: NavHostController) {

    val authRepository = FirebaseAuthRepository()

    // UseCases
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val getUsernameUseCase = GetUsernameUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val updateUsernameUseCase = UpdateUsernameUseCase(authRepository)
    val updateEmailUseCase = UpdateEmailUseCase(authRepository)
    val updatePasswordUseCase = UpdatePasswordUseCase(authRepository)
    val reauthenticateUseCase = ReauthenticateUseCase(authRepository)


    val viewModel: ProfileScreenVM = viewModel(
        factory = ProfileScreenVMFactory(
            getCurrentUserUseCase,
            getUsernameUseCase,
            logoutUseCase,
            updateUsernameUseCase,
            updateEmailUseCase,
            updatePasswordUseCase,
            reauthenticateUseCase
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navegación automática al cerrar sesión
    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            navController.navigate(RouteGeneral.LoginScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    ProfileContent(
        uiState = uiState,
        onLogoutClick = { viewModel.onLogoutClicked() },
        onUpdateUsername = { viewModel.updateUsername(it) },
        onUpdateEmail = { viewModel.updateEmail(it) },
        onUpdatePassword = { current, new, confirm, onResult ->
            viewModel.updatePassword(current, new, confirm, onResult)
        }
    )
}
