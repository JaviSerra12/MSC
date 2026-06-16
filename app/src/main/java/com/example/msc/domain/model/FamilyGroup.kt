package com.example.msc.domain.model

import com.google.firebase.firestore.DocumentId

data class FamilyGroup(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val adminId: String = "",
    val members: List<String> = emptyList()
)
