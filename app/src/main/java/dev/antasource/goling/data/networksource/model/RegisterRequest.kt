package dev.antasource.goling.data.networksource.model

data class RegisterRequest(
    val username : String,
    val email : String,
    val password: String,
    val phone: String
)
