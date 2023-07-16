package net.meilcli.hubber.authentication.ui.main.login.method.select

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LoginMethodSelectPage()
}

@Composable
private fun LoginMethodSelectPage() {
    Text(text = "aaa")
}
