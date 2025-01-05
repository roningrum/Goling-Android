package dev.antasource.goling.core.navigation

sealed class GolingScreen(val route: String) {
    object SplashScreenCustom: GolingScreen(route = "Splash_Screen")
    object Onboarding: GolingScreen(route = "Onboarding")
    object LoginScreen: GolingScreen(route = "Login")
    object RegisterScreen: GolingScreen(route = "Register")
}