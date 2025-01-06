package dev.antasource.goling.ui.feature.walkthrough

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.antasource.goling.R
import dev.antasource.goling.core.navigation.GolingScreen
import dev.antasource.goling.core.ui.component.ButtonPrimary
import dev.antasource.goling.core.ui.component.DotsIndicator
import dev.antasource.goling.core.ui.component.OutlineButton
import dev.antasource.goling.core.ui.component.TermsConditionText
import dev.antasource.goling.ui.feature.ui.theme.whiteBackground

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navHostController: NavHostController) {
    val slideImage = remember { mutableIntStateOf(R.drawable.onboarding_pic) }
    val state = rememberPagerState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(count = 2, state = state) { page ->
            OnboardingPageContent(page, slideImage)

        }
        DotsIndicator(
            totalDots = 2,
            selectedIndex = state.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 48.dp, end = 16.dp)
        ) {
            // Login Button
            ButtonPrimary(
                "Masuk",
                Modifier
                    .align(Alignment.TopStart)
                    .width(160.dp)
                    .height(56.dp),
                onClick = {
                    navHostController.navigate(GolingScreen.LoginScreen.route)
                }
            )
            OutlineButton(
                stringResource(id = R.string.register_button_text),
                Modifier
                    .align(Alignment.TopEnd)
                    .width(160.dp)
                    .height(56.dp),
                onClick = {
                    navHostController.navigate(GolingScreen.LoginScreen)
                }
            )
        }

        // Term & Conditions Text
        TermsConditionText(
            {},
            {},
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 20.dp, end = 16.dp)
        )
    }

}


@Composable
fun OnboardingPageContent(page: Int, sliderImage: MutableIntState) {
    // Example of content for each page
    // Customize this with actual content for each onboarding page

    when (page) {
        0 -> {
            sliderImage.value = R.drawable.onboarding_pic
        }

        1 -> {
            sliderImage.value = R.drawable.onboarding_pic_second
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(0.dp)
    ) {
        Image(
            painter = painterResource(id = sliderImage.value),
            contentDescription = "View Pager",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }

}

@Preview
@Composable
fun OnboardingPreview() {
    val navHostController = rememberNavController()
    OnboardingScreen(navHostController)
}
