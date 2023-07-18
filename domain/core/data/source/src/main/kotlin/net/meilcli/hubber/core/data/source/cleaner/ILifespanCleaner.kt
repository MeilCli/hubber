package net.meilcli.hubber.core.data.source.cleaner

interface ILifespanCleaner {

    suspend fun clearByLogout()

    suspend fun clearByTaskKill()
}
