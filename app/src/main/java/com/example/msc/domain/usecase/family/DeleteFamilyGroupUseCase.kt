package com.example.msc.domain.usecase.family

import com.example.msc.domain.repository.FamilyRepository

class DeleteFamilyGroupUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(familyGroupId: String): Result<Unit> {
        return repository.deleteFamilyGroup(familyGroupId)
    }
}
