package net.meilcli.hubber.splash.ui.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.splash.contract.main.ISplashMainContract
import net.meilcli.hubber.splash.contract.main.SplashMainNavGraph
import net.meilcli.hubber.splash.ui.main.splash.SplashPageHost

class SplashMainContract : ISplashMainContract {

    override val navGraph: NavGraphSpec
        get() = SplashMainNavGraph

    override fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>) {
        dependenciesContainerBuilder.apply {
            dependency<ISplashMainContract, T>(this@SplashMainContract)
        }
    }

    @Composable
    override fun SplashPage(navigator: DestinationsNavigator) {
        SplashPageHost(navigator = navigator)
    }
}
