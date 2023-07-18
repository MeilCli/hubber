package net.meilcli.hubber.core.data.source.cleaner

internal class LifespanCleaner(
    private val memoryStorageCleaner: ILifespanCleaner,
    private val dataStoreCleaner: ILifespanCleaner,
    private val fileSystemCleaner: ILifespanCleaner
) : ILifespanCleaner {

    override suspend fun clearByLogout() {
        memoryStorageCleaner.clearByLogout()
        dataStoreCleaner.clearByLogout()
        fileSystemCleaner.clearByLogout()
    }

    override suspend fun clearByTaskKill() {
        memoryStorageCleaner.clearByTaskKill()
        dataStoreCleaner.clearByTaskKill()
        fileSystemCleaner.clearByTaskKill()
    }
}
