package net.meilcli.hubber.home.ui.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.home.contract.main.HomeMainNavGraph
import net.meilcli.hubber.home.contract.main.IHomeMainContract
import net.meilcli.hubber.home.ui.main.home.HomePageHost

class HomeMainContract : IHomeMainContract {

    override val navGraph: NavGraphSpec
        get() = HomeMainNavGraph

    override fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>) {
        dependenciesContainerBuilder.apply {
            dependency<IHomeMainContract, T>(this@HomeMainContract)
        }
    }

    @Composable
    override fun HomePage(navigator: DestinationsNavigator) {
        HomePageHost(navigator = navigator)
    }
}
