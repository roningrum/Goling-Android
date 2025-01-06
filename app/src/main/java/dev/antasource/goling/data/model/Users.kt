package dev.antasource.goling.data.model

data class Users(
    var createdAt: String,
    var email: String,
    var id: Int,
    var isLoggedIn: Boolean,
    var password: String,
    var phone: String,
    var photo: String,
    var resetPasswordExpires: String,
    var resetPasswordToken: String,
    var role: String,
    var updatedAt: String,
    var username: String
)
