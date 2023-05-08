package net.meilcli.hubber.core.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.NavGraphSpec
import net.meilcli.hubber.core.contract.main.Contracts

abstract class HubberActivity : AppCompatActivity() {

    abstract val navGraph: NavGraphSpec

    abstract val contracts: Contracts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DestinationsNavHost(
                navGraph = navGraph,
                dependenciesContainerBuilder = {
                    contracts.value.forEach {
                        it.addDependency(this)
                    }
                }
            )
        }
    }
}
