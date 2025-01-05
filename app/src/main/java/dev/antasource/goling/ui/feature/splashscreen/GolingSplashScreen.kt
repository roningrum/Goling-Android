package dev.antasource.goling.ui.feature.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.antasource.goling.ui.feature.ui.theme.whiteBackground
import dev.antasource.goling.R
import dev.antasource.goling.core.navigation.GolingScreen
import dev.antasource.goling.core.ui.SetLightStatusBar
import kotlinx.coroutines.delay

@Composable
fun GolingSplashScreen(navHostController: NavHostController) {
    SetLightStatusBar()
    LaunchedEffect(Unit) {
        delay(3000)
        navHostController.navigate(GolingScreen.Onboarding.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBackground)
    ){
        Image(
            painter = painterResource(id = R.drawable.goling_app_logo),
            contentDescription = stringResource(id = R.string.content_desc_logo_img),
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = stringResource(R.string.version_goling_app),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

//@Preview
//@Composable
//fun GolingScreenPreview(){
//    GolingSplashScreen(navHostController)
//}