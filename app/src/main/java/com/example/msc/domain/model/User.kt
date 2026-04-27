package com.example.msc.domain.model

/**
 * Modelo que representa la información de perfil de un usuario en el sistema.
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = ""
)
