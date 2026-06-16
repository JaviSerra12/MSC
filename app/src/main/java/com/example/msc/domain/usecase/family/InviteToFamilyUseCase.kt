package com.example.msc.domain.usecase.family

import com.example.msc.domain.repository.FamilyRepository

class InviteToFamilyUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(email: String, currentUserId: String): Result<Unit> {
        return repository.inviteUserByEmail(email, currentUserId)
    }
}
