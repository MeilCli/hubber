package net.meilcli.hubber.authentication.ui.main.login.method.select

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.authentication.ui.main.destinations.LoginPageDestination
import net.meilcli.hubber.core.ui.main.extensions.popBackStackOrFinishActivity

data class LoginMethodSelectPageState(
    val authenticateAsGuest: () -> Unit,
    val authenticateAsUser: () -> Unit,
    val backNavigation: () -> Unit
)

@Composable
fun rememberLoginMethodSelectPageState(
    pageViewModel: LoginMethodSelectPageViewModel,
    navigator: DestinationsNavigator
): LoginMethodSelectPageState {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    return remember(pageViewModel, navigator, onBackPressedDispatcher) {
        LoginMethodSelectPageState(
            authenticateAsGuest = pageViewModel::authenticateAsGuest,
            authenticateAsUser = { navigator.navigate(LoginPageDestination) },
            backNavigation = { navigator.popBackStackOrFinishActivity(onBackPressedDispatcher) }
        )
    }
}
