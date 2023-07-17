package net.meilcli.hubber.core.contract.main

import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.spec.NavGraphSpec

interface IContract {

    val navGraph: NavGraphSpec

    val internalNavGraph: NavGraphSpec?
        get() = null

    fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>)
}
