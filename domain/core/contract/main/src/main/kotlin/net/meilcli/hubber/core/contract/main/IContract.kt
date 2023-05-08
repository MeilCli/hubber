package net.meilcli.hubber.core.contract.main

import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.spec.NavGraphSpec

interface IContract {

    val navGraph: NavGraphSpec

    fun <T> addDependency(dependenciesContainerBuilder: DependenciesContainerBuilder<T>)
}
