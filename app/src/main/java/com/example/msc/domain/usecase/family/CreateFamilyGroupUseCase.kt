package com.example.msc.domain.usecase.family

import com.example.msc.domain.repository.FamilyRepository

class CreateFamilyGroupUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(name: String, adminId: String): Result<String> {
        return repository.createFamilyGroup(name, adminId)
    }
}
