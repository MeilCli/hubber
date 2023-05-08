package net.meilcli.hubber

import net.meilcli.hubber.core.contract.main.Contracts
import net.meilcli.hubber.core.ui.main.CoreMainContract
import net.meilcli.hubber.splash.ui.main.SplashMainContract

val MainContracts = Contracts(
    listOf(
        CoreMainContract(),
        SplashMainContract()
    )
)
