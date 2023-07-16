package net.meilcli.hubber

import net.meilcli.hubber.authentication.ui.main.AuthenticationMainContract
import net.meilcli.hubber.core.contract.main.Contracts
import net.meilcli.hubber.splash.ui.main.SplashMainContract

val MainContracts = Contracts(
    listOf(
        SplashMainContract(),
        AuthenticationMainContract()
    )
)
