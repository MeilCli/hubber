package net.meilcli.hubber.authentication.ui.main.login.method.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.splash.contract.main.destinations.SplashPageDestination

@Composable
fun LoginMethodSelectPageHost(
    pageViewModel: LoginMethodSelectPageViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(pageViewModel) {
        pageViewModel.event.collect {
            when (it) {
                LoginMethodSelectPageViewModel.Event.AuthenticateCompleteAsGuest -> {
                    navigator.navigate(SplashPageDestination)
                }
            }
        }
    }

    val pageState = rememberLoginMethodSelectPageState(
        pageViewModel = pageViewModel,
        navigator = navigator
    )

    LoginMethodSelectPage(pageState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginMethodSelectPage(pageState: LoginMethodSelectPageState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Choose Login")
                },
                navigationIcon = {
                    IconButton(
                        onClick = pageState.backNavigation
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = pageState.authenticateAsGuest) {
                Text(text = "Login as Guest")
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    MaterialTheme {
        LoginMethodSelectPage(
            pageState = LoginMethodSelectPageState(
                authenticateAsGuest = {},
                backNavigation = {}
            )
        )
    }
}
