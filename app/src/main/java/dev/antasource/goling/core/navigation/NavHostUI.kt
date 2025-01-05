package dev.antasource.goling.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.antasource.goling.ui.feature.splashscreen.GolingSplashScreen
import dev.antasource.goling.ui.feature.walkthrough.OnboardingScreen

@Composable
fun customNavHost(navHostController: NavHostController){
    NavHost(navHostController, startDestination = GolingScreen.SplashScreenCustom.route) {
      composable(route = GolingScreen.SplashScreenCustom.route){
          GolingSplashScreen(navHostController)
      }
        composable(route = "Onboarding"){
            OnboardingScreen(navHostController)
        }
    }
}