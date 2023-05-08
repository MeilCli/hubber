package net.meilcli.hubber.core.contract.main

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

interface ICoreMainContract : IContract {

    @Composable
    fun CoreDummyPage()
}

@Destination
@RootNavGraph(start = true)
@Composable
fun CoreDummy(contract: ICoreMainContract) {
    contract.CoreDummyPage()
}
