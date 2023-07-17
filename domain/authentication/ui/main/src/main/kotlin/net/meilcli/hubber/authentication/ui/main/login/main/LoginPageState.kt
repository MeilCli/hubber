package net.meilcli.hubber.authentication.ui.main.login.main

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.core.ui.main.extensions.popBackStackOrFinishActivity

data class LoginPageState(
    val backNavigation: () -> Unit
)

@Composable
fun rememberLoginPageState(navigator: DestinationsNavigator): LoginPageState {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    return remember(navigator, onBackPressedDispatcher) {
        LoginPageState(
            backNavigation = { navigator.popBackStackOrFinishActivity(onBackPressedDispatcher) }
        )
    }
}
