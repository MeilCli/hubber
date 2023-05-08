package net.meilcli.hubber.splash.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.core.contract.main.destinations.CoreDummyDestination
import net.meilcli.hubber.splash.contract.main.ISplashMainContract
import net.meilcli.hubber.splash.contract.main.SplashMainNavGraph

class SplashMainContract : ISplashMainContract {

    override val navGraph: NavGraphSpec
        get() = SplashMainNavGraph

    override fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>) {
        dependenciesContainerBuilder.apply {
            dependency<ISplashMainContract, T>(this@SplashMainContract)
        }
    }

    @Composable
    override fun SplashDummyPage(navigator: DestinationsNavigator) {
        Column {
            Text(text = "SplashDummy")
            Button(onClick = { navigator.navigate(CoreDummyDestination) }) {
                Text(text = "GoToCoreDummy")
            }
        }
    }
}
