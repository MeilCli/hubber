package net.meilcli.hubber

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import net.meilcli.hubber.splash.contract.main.SplashMainNavGraph

object MainNavGraph : NavGraphSpec {

    override val route: String = "main"

    override val destinationsByRoute: Map<String, DestinationSpec<*>> = emptyMap()

    override val startRoute: Route = SplashMainNavGraph

    override val nestedNavGraphs: List<NavGraphSpec> = MainContracts.value.map { it.navGraph }
}
