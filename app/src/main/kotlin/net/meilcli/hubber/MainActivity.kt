package net.meilcli.hubber

import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.AndroidEntryPoint
import net.meilcli.hubber.core.contract.main.Contracts
import net.meilcli.hubber.core.ui.main.HubberActivity

@AndroidEntryPoint
class MainActivity : HubberActivity() {

    override val navGraph: NavGraphSpec
        get() = MainNavGraph

    override val contracts: Contracts
        get() = MainContracts
}
