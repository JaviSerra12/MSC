package com.example.msc.ui.screen.profileScreen.familyGroup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.msc.data.repository.FirebaseAuthRepository
import com.example.msc.data.repository.FirebaseFamilyRepository
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.*
import com.example.msc.ui.components.Buttons.ActionItem
import com.example.msc.ui.components.Buttons.ActionsDropdown
import com.example.msc.ui.components.PopUpWindows.GeneralConfirmationDialog
import com.example.msc.ui.components.PopUpWindows.EditFieldDialog
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyGroupScreen(navController: NavHostController, familyGroupId: String) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val familyRepository = FirebaseFamilyRepository(db)
    val authRepository = FirebaseAuthRepository()

    val viewModel: FamilyGroupVM = viewModel(
        factory = FamilyGroupVMFactory(
            familyGroupId = familyGroupId,
            getFamilyGroupFlowUseCase = GetFamilyGroupFlowUseCase(familyRepository),
            getUsernameUseCase = GetUsernameUseCase(authRepository),
            updateGroupNameUseCase = UpdateGroupNameUseCase(familyRepository),
            inviteToFamilyUseCase = InviteToFamilyUseCase(familyRepository),
            removeFamilyMemberUseCase = RemoveFamilyMemberUseCase(familyRepository),
            deleteFamilyGroupUseCase = DeleteFamilyGroupUseCase(familyRepository),
            getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showEditName by remember { mutableStateOf(false) }
    var showInviteDialog by remember { mutableStateOf(false) }

    val isAdmin = uiState.adminId == viewModel.currentUserId

    LaunchedEffect(uiState.isGroupDeleted) {
        if (uiState.isGroupDeleted) {
            navController.popBackStack()
        }
    }

    if (uiState.isDeleteDialogVisible) {
        GeneralConfirmationDialog(
            title = "¿Eliminar grupo?",
            text = "¿Estás seguro de que quieres eliminar este grupo? Esta acción no se puede deshacer.",
            onDismiss = { viewModel.onDismissDeleteDialog() },
            onConfirm = { viewModel.onConfirmDelete() }
        )
    }

    if (uiState.isLeaveDialogVisible) {
        GeneralConfirmationDialog(
            title = "¿Abandonar grupo?",
            text = "¿Estás seguro de que quieres abandonar este grupo?",
            confirmButtonText = "Abandonar",
            confirmButtonColor = BlueMSC,
            onDismiss = { viewModel.onDismissLeaveDialog() },
            onConfirm = { viewModel.onConfirmLeave() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Familia", fontFamily = dosisRegular) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueMSC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BlueMSC)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Group Header Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = uiState.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = dosisRegular,
                                color = BlueMSC
                            )
                            if (uiState.isEditMode) {
                                IconButton(onClick = { showEditName = true }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar nombre",
                                        tint = BlueMSC,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Administrador: ${uiState.adminName}",
                            fontSize = 14.sp,
                            fontFamily = dosisRegular,
                            color = Color.Gray
                        )
                    }

                    ActionsDropdown(
                        actions = if (isAdmin) {
                            listOf(
                                ActionItem(label = "Editar") { viewModel.onEditClicked() },
                                ActionItem(label = "Borrar") { viewModel.onDeleteClicked() }
                            )
                        } else {
                            listOf(
                                ActionItem(label = "Abandonar") { viewModel.onLeaveClicked() }
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Miembros",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dosisRegular,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.members) { member ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (uiState.isEditMode && member.uid != uiState.adminId) Color(
                                    0xFFFFEBEE
                                ) else Color(0xFFF5F5F5)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = member.username,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = dosisRegular
                                    )
                                    Text(text = member.email, fontSize = 12.sp, color = Color.Gray)
                                }
                                if (uiState.isEditMode && member.uid != uiState.adminId) {
                                    IconButton(onClick = { viewModel.removeMember(member.uid) }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Eliminar",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { showInviteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BlueMSC)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Invitar Miembro", fontFamily = dosisRegular)
                        }
                    }
                }
            }
        }
    }


    if (showEditName) {
        EditFieldDialog(
            title = "Cambiar nombre del grupo",
            initialValue = uiState.name,
            onDismiss = { showEditName = false },
            onConfirm = {
                viewModel.updateGroupName(it)
                showEditName = false
            }
        )
    }

    if (showInviteDialog) {
        EditFieldDialog(
            title = "Invitar por email",
            initialValue = "",
            onDismiss = { showInviteDialog = false },
            onConfirm = { email ->
                viewModel.inviteMember(email) { result ->
                    if (result.isSuccess) {
                        Toast.makeText(context, "Invitación enviada", Toast.LENGTH_SHORT).show()
                        showInviteDialog = false
                    } else {
                        Toast.makeText(
                            context,
                            result.exceptionOrNull()?.message ?: "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            keyboardType = KeyboardType.Email
        )
    }
}
