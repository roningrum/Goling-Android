package dev.antasource.goling.ui.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.antasource.goling.core.navigation.customNavHost
import dev.antasource.goling.ui.feature.ui.theme.GolingAndroidTheme

class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        enableEdgeToEdge()
        installSplashScreen()
        actionBar?.hide()
        setContent {
            GolingAndroidTheme() {
//                SetLightStatusBar()
                navHostController = rememberNavController()
                customNavHost(navHostController)
            }
        }
    }
}

//@Composable
//private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
//    val barColor = MaterialTheme.colorScheme.background.toArgb()
//    LaunchedEffect(lightTheme) {
//        if (lightTheme) {
//
//            )
//        } else {
//            enableEdgeToEdge(
//                statusBarStyle = SystemBarStyle.dark(
//                    barColor,
//                ),
//                navigationBarStyle = SystemBarStyle.dark(
//                    barColor,
//                ),
//            )
//        }
//    }
//}
