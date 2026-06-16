package com.example.msc.ui.screen.profileScreen.familyGroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.*

class FamilyGroupVMFactory(
    private val familyGroupId: String,
    private val getFamilyGroupFlowUseCase: GetFamilyGroupFlowUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val updateGroupNameUseCase: UpdateGroupNameUseCase,
    private val inviteToFamilyUseCase: InviteToFamilyUseCase,
    private val removeFamilyMemberUseCase: RemoveFamilyMemberUseCase,
    private val deleteFamilyGroupUseCase: DeleteFamilyGroupUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FamilyGroupVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FamilyGroupVM(
                familyGroupId,
                getFamilyGroupFlowUseCase,
                getUsernameUseCase,
                updateGroupNameUseCase,
                inviteToFamilyUseCase,
                removeFamilyMemberUseCase,
                deleteFamilyGroupUseCase,
                getCurrentUserUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
