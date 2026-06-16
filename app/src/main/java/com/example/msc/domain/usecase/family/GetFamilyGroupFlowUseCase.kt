package com.example.msc.domain.usecase.family

import com.example.msc.domain.model.FamilyGroup
import com.example.msc.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow

class GetFamilyGroupFlowUseCase(private val repository: FamilyRepository) {
    operator fun invoke(familyGroupId: String): Flow<FamilyGroup?> {
        return repository.getFamilyGroupFlow(familyGroupId)
    }
}
