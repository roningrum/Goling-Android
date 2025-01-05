package dev.antasource.goling.ui.feature.walkthrough

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dev.antasource.goling.R
import dev.antasource.goling.ui.feature.ui.theme.Typography
import dev.antasource.goling.ui.feature.ui.theme.bluePrimary
import dev.antasource.goling.ui.feature.ui.theme.disableGrey
import dev.antasource.goling.ui.feature.ui.theme.whiteBackground

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navHostController: NavHostController) {
    val slideImage = remember { mutableIntStateOf(R.drawable.onboarding_pic) }
    val state = rememberPagerState()
    Column( modifier = Modifier.fillMaxSize().background(whiteBackground), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(count = 2, state = state) { page->
            OnboardingPageContent(page, slideImage)

        }
        DotsIndicator(
            totalDots = 2,
            selectedIndex = state.currentPage,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 64.dp, end = 16.dp)
        ) {
            // Login Button
            ElevatedButton(
                colors = ButtonDefaults.buttonColors(bluePrimary),
                onClick = { /* Handle login */ },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .width(160.dp)
                    .height(56.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.login_button_text),
                        color = whiteBackground
                    )
                }
            )

            // Sign Up Button
            OutlinedButton(
                onClick = { /* Handle sign up */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(160.dp)
                    .height(56.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.register_button_text),
                        color = bluePrimary
                    )
                },
                border = BorderStroke(1.dp, bluePrimary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor =whiteBackground
                )
            )
        }



        // Term & Conditions Text
        Text(
            text = "Dengan mendaftar saya telah menyetujui\nSyarat & Ketentuan dan Kebijakan Privasi GoLing",
            textAlign = TextAlign.Center,
            style = Typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 20.dp, end = 16.dp)
        )  // Adjust the padding here
    }

}

@Composable
fun DotsIndicator(
    totalDots : Int,
    selectedIndex : Int,
){

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center

        ) {
        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(bluePrimary)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(disableGrey)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: Int, sliderImage: MutableIntState) {
    // Example of content for each page
    // Customize this with actual content for each onboarding page

    when(page){
        0 ->{
            sliderImage.value = R.drawable.onboarding_pic
        }
        1->{
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
            painter = painterResource(id =sliderImage.value),
            contentDescription = "View Pager",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().align(Alignment.Center)
        )
    }

}
@Preview
@Composable
fun OnboardingPreview(){
    val navHostController = rememberNavController()
    OnboardingScreen(navHostController)
}
