package com.example.msc.domain.repository

import com.example.msc.domain.model.FamilyGroup
import com.example.msc.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FamilyRepository {
    suspend fun createFamilyGroup(name: String, adminId: String): Result<String>
    suspend fun inviteUserByEmail(email: String, familyGroupId: String): Result<Unit>
    fun getFamilyGroupFlow(familyGroupId: String): Flow<FamilyGroup?>
    suspend fun getFamilyGroup(familyGroupId: String): FamilyGroup?
    suspend fun getUserById(uid: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateGroupName(familyGroupId: String, newName: String): Result<Unit>
    suspend fun removeMember(familyGroupId: String, userId: String): Result<Unit>
    suspend fun deleteFamilyGroup(familyGroupId: String): Result<Unit>
}
