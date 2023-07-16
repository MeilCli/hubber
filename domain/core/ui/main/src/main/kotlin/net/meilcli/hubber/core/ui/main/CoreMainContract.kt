package net.meilcli.hubber.core.ui.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.core.contract.main.CoreMainNavGraph
import net.meilcli.hubber.core.contract.main.ICoreMainContract
import net.meilcli.hubber.core.ui.main.test.page.TestPage

class CoreMainContract : ICoreMainContract {

    override val navGraph: NavGraphSpec
        get() = CoreMainNavGraph

    override fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>) {
        dependenciesContainerBuilder.apply {
            dependency<ICoreMainContract, T>(this@CoreMainContract)
        }
    }

    @Composable
    override fun CoreDummyPage() {
        TestPage()
    }
}
