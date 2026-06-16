package com.example.msc.domain.usecase.family

import com.example.msc.domain.repository.FamilyRepository

class RemoveFamilyMemberUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(familyGroupId: String, userId: String): Result<Unit> {
        return repository.removeMember(familyGroupId, userId)
    }
}
