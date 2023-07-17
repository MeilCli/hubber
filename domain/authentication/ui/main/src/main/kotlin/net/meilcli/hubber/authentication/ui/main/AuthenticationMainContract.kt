package net.meilcli.hubber.authentication.ui.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.authentication.contract.main.AuthenticationMainNavGraph
import net.meilcli.hubber.authentication.contract.main.IAuthenticationMainContract
import net.meilcli.hubber.authentication.ui.main.login.main.LoginPageHost
import net.meilcli.hubber.authentication.ui.main.login.method.select.LoginMethodSelectPageHost

class AuthenticationMainContract : IAuthenticationMainContract {

    override val navGraph: NavGraphSpec
        get() = AuthenticationMainNavGraph

    override val internalNavGraph: NavGraphSpec
        get() = AuthenticationMainInternalNavGraph

    override fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>) {
        dependenciesContainerBuilder.apply {
            dependency<IAuthenticationMainContract, T>(this@AuthenticationMainContract)
        }
    }

    @Composable
    override fun LoginMethodSelectPage(navigator: DestinationsNavigator) {
        LoginMethodSelectPageHost(navigator = navigator)
    }
}

@Destination
@RootNavGraph(start = true)
@Composable
fun LoginPage(navigator: DestinationsNavigator) {
    LoginPageHost(navigator = navigator)
}
