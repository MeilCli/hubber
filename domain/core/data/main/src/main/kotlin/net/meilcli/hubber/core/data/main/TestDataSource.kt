package net.meilcli.hubber.core.data.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.core.data.main.internal.cache.TestCache
import javax.inject.Singleton

interface ITestDataSource {

    fun hello(): String
}

@Module
@InstallIn(SingletonComponent::class)
object TestDataSourceModule {

    @Provides
    @Singleton
    fun provideTestDataSource(): ITestDataSource {
        return TestCache()
    }
}
