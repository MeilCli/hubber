package net.meilcli.hubber.authentication.contract.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import net.meilcli.hubber.core.contract.main.IContract

interface IAuthenticationMainContract : IContract {

    @Composable
    fun LoginMethodSelectPage(navigator: DestinationsNavigator)
}

@Destination
@RootNavGraph(start = true)
@Composable
fun LoginMethodSelectPage(contract: IAuthenticationMainContract, navigator: DestinationsNavigator) {
    contract.LoginMethodSelectPage(navigator)
}
