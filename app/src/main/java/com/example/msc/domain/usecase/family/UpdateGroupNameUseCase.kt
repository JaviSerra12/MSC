package com.example.msc.domain.usecase.family

import com.example.msc.domain.repository.FamilyRepository

class UpdateGroupNameUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(familyGroupId: String, newName: String): Result<Unit> {
        return repository.updateGroupName(familyGroupId, newName)
    }
}
