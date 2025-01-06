package dev.antasource.goling.ui.feature.login

sealed class LoginUiState{
    object Idle: LoginUiState()
    data class Success (val message: String?, val token: String?): LoginUiState()
    data class Error (val message: String): LoginUiState()
}