package net.meilcli.hubber.home.ui.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.ramcosta.composedestinations.navigation.popUpTo
import net.meilcli.hubber.home.contract.main.destinations.HomePageDestination
import net.meilcli.hubber.splash.contract.main.destinations.SplashPageDestination

@Composable
fun HomePageHost(
    pageViewModel: HomePageViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(pageViewModel) {
        pageViewModel.event.collect {
            when (it) {
                HomePageViewModel.Event.LogoutComplete -> {
                    navigator.navigate(SplashPageDestination) {
                        popUpTo(HomePageDestination) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    val pageState = rememberHomePageState(pageViewModel = pageViewModel)

    HomePage(pageState = pageState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomePage(pageState: HomePageState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Home")
                },
                modifier = Modifier
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = pageState.logout) {
                Text(text = "Logout")
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    MaterialTheme {
        HomePage(pageState = HomePageState(logout = {}))
    }
}
