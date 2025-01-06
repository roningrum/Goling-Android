package dev.antasource.goling.ui.feature.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dev.antasource.goling.R
import dev.antasource.goling.core.ui.component.ButtonPrimary
import dev.antasource.goling.core.ui.component.TextOnlyButton
import dev.antasource.goling.ui.feature.login.viewmodel.LoginViewModel
import dev.antasource.goling.ui.feature.ui.theme.Typography
import dev.antasource.goling.ui.feature.ui.theme.darkBlue
import dev.antasource.goling.util.SharedPrefUtil

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val user = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginUiState = loginViewModel.loginState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(loginUiState) {
        when (loginUiState) {
            is LoginUiState.Success -> {
                val message = (loginUiState as LoginUiState.Success).message
                val token = loginUiState.token
                Log.d("Message", "$message")
                message?.let {it ->
                    snackbarHostState.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short
                    )
                }

                SharedPrefUtil.saveAccessToken(context, token.toString())
            }
            is LoginUiState.Error ->{
                val message = (loginUiState as LoginUiState.Error).message
                message.let{ it->
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }

            }
        }
    }



    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)},
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.goling_app_logo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.login_subtitle_text),
                        style = Typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        value = user.value,
                        onValueChange = { user.value = it },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text("Username", color = darkBlue) }

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        value = password.value,
                        shape = RoundedCornerShape(8.dp),
                        onValueChange = { password.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        label = { Text("Password", color = darkBlue) }

                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextOnlyButton(
                        textButton = "Lupa Sandi",
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(0.dp),
                        onClick = {}
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Login Button Section
                ButtonPrimary(
                    textButton = "Login",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        loginViewModel.loginGoling(user.value, password.value)
                    }
                )
            }
        }
    }
}
