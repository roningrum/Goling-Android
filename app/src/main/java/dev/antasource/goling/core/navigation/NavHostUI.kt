package dev.antasource.goling.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.antasource.goling.data.networksource.NetworkRemoteSource
import dev.antasource.goling.data.repositoty.AuthenticationRepository
import dev.antasource.goling.ui.factory.AuthViewModelFactory
import dev.antasource.goling.ui.feature.login.LoginScreen
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import dev.antasource.goling.ui.feature.splashscreen.GolingSplashScreen
import dev.antasource.goling.ui.feature.walkthrough.OnboardingScreen

@Composable
fun CustomNavHost(navHostController: NavHostController) {
    NavHost(navHostController, startDestination = GolingScreen.SplashScreenCustom.route) {
        composable(route = GolingScreen.SplashScreenCustom.route) {
            GolingSplashScreen(navHostController)
        }
        composable(route = "Onboarding") {
            OnboardingScreen(navHostController)
        }
        composable(route = GolingScreen.LoginScreen.route) {
            val networkRemoteSource = NetworkRemoteSource()
            val authRepo = AuthenticationRepository(networkRemoteSource)
            val loginViewModel = ViewModelProvider(
                LocalContext.current as ViewModelStoreOwner,
                AuthViewModelFactory(authRepo)
            )[LoginViewModel::class.java]
            LoginScreen(navHostController, loginViewModel)
        }

    }
}