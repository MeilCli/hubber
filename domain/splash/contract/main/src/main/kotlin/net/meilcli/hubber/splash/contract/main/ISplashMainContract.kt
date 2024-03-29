package net.meilcli.hubber.splash.contract.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.core.contract.main.IContract

interface ISplashMainContract : IContract {

    @Composable
    fun SplashPage(navigator: DestinationsNavigator)
}

@Destination
@RootNavGraph(start = true)
@Composable
fun SplashPage(contract: ISplashMainContract, navigator: DestinationsNavigator) {
    contract.SplashPage(navigator)
}
