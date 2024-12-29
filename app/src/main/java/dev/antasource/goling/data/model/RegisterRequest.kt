package dev.antasource.goling.data.model

data class RegisterRequest(
    val username : String,
    val email : String,
    val password: String,
    val phone: String
)
