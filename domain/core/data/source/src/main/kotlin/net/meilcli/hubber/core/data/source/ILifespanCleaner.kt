package net.meilcli.hubber.core.data.source

interface ILifespanCleaner {

    suspend fun clearByLogout()

    suspend fun clearByTaskKill()
}
