package net.meilcli.hubber.home.ui.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class HomePageState(
    val logout: () -> Unit
)

@Composable
fun rememberHomePageState(pageViewModel: HomePageViewModel): HomePageState {
    return remember(pageViewModel) {
        HomePageState(
            logout = pageViewModel::logout
        )
    }
}
