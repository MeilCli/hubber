package net.meilcli.hubber.core.data.source.store

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.core.data.source.cleaner.DataStoreCleaner
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import javax.inject.Singleton

interface IDataStoreContainer {

    fun lifeSpan(lifespan: Lifespan): IDataStore
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreContainerModule {

    @Provides
    @Singleton
    fun provideDataStoreContainer(
        @ApplicationContext
        context: Context
    ): IDataStoreContainer {
        return DataStoreContainer(context)
    }

    @DataStoreCleaner
    @Provides
    @Singleton
    fun provideMemoryStorageContainerCleaner(
        dataStoreContainer: IDataStoreContainer
    ): ILifespanCleaner {
        return dataStoreContainer as DataStoreContainer
    }
}
