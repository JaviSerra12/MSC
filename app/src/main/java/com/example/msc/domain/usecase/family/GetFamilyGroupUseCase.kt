package com.example.msc.domain.usecase.family

import com.example.msc.domain.model.FamilyGroup
import com.example.msc.domain.repository.FamilyRepository

class GetFamilyGroupUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(familyGroupId: String): FamilyGroup? {
        return repository.getFamilyGroup(familyGroupId)
    }
}
