package dev.antasource.goling.data.networksource.model

data class UserRequest(
    val username:String,
    val email: String,
    val password: String,
    val role: String
)
