package net.meilcli.hubber.home.contract.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.core.contract.main.IContract

interface IHomeMainContract : IContract {

    @Composable
    fun HomePage(navigator: DestinationsNavigator)
}

@Destination
@RootNavGraph(start = true)
@Composable
fun HomePage(contract: IHomeMainContract, navigator: DestinationsNavigator) {
    contract.HomePage(navigator)
}
