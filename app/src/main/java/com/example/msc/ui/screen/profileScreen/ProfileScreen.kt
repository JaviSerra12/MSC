package com.example.msc.ui.screen.profileScreen

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.msc.data.repository.FirebaseAuthRepository
import com.example.msc.data.repository.FirebaseFamilyRepository
import com.example.msc.domain.usecase.auth.*
import com.example.msc.domain.usecase.family.CreateFamilyGroupUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import com.example.msc.ui.components.profile.ProfileContent
import com.example.msc.ui.navigation.RouteGeneral
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavHostController) {

    val db = FirebaseFirestore.getInstance()
    val authRepository = FirebaseAuthRepository()
    val familyRepository = FirebaseFamilyRepository(db)

    // UseCases
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val getUsernameUseCase = GetUsernameUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val updateUsernameUseCase = UpdateUsernameUseCase(authRepository)
    val updateEmailUseCase = UpdateEmailUseCase(authRepository)
    val updatePasswordUseCase = UpdatePasswordUseCase(authRepository)
    val reauthenticateUseCase = ReauthenticateUseCase(authRepository)
    
    val getFamilyGroupUseCase = GetFamilyGroupUseCase(familyRepository)
    val createFamilyGroupUseCase = CreateFamilyGroupUseCase(familyRepository)

    val viewModel: ProfileScreenVM = viewModel(
        factory = ProfileScreenVMFactory(
            getCurrentUserUseCase,
            getUsernameUseCase,
            logoutUseCase,
            updateUsernameUseCase,
            updateEmailUseCase,
            updatePasswordUseCase,
            reauthenticateUseCase,
            getFamilyGroupUseCase,
            createFamilyGroupUseCase
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

    // Recarga los datos al volver a la pantalla para mostrar los cambios en el grupo
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    ProfileContent(
        uiState = uiState,
        onLogoutClick = { viewModel.onLogoutClicked() },
        onUpdateUsername = { viewModel.updateUsername(it) },
        onUpdateEmail = { viewModel.updateEmail(it) },
        onUpdatePassword = { current, new, confirm, onResult ->
            viewModel.updatePassword(current, new, confirm, onResult)
        },
        onCreateFamilyGroup = { name ->
            viewModel.createFamilyGroup(name) { result ->
                result.onSuccess { id ->
                    navController.navigate(RouteGeneral.FamilyGroupScreen.createRoute(id))
                }
            }
        },
        onManageFamilyGroup = {
            uiState.familyGroupId?.let { id ->
                navController.navigate(RouteGeneral.FamilyGroupScreen.createRoute(id))
            }
        }
    )
}
