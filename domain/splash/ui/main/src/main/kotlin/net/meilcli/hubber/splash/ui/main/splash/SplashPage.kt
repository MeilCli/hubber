package net.meilcli.hubber.splash.ui.main.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.authentication.contract.main.destinations.LoginMethodSelectPageDestination
import net.meilcli.hubber.authentication.data.main.entity.Authentication

@Composable
fun SplashPageHost(
    pageViewModel: SplashPageViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val authentication = pageViewModel.authentication
    LaunchedEffect(authentication) {
        when (authentication) {
            is Authentication.Never -> navigator.navigate(LoginMethodSelectPageDestination)
            else -> {}
        }
    }

    LaunchedEffect(pageViewModel) {
        pageViewModel.loadAuthentication()
    }

    SplashPage()
}

@Composable
private fun SplashPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    MaterialTheme {
        SplashPage()
    }
}
