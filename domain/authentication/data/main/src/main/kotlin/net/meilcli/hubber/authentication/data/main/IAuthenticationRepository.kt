package net.meilcli.hubber.authentication.data.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.authentication.data.main.entity.Authentication
import net.meilcli.hubber.authentication.data.main.internal.combine.AuthenticationCombinator
import net.meilcli.hubber.authentication.data.main.internal.memory.AuthenticationMemoryStorage
import net.meilcli.hubber.core.data.source.cleaner.CompositeLifespanCleaner
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import net.meilcli.hubber.core.data.source.memory.IMemoryStorageContainer
import javax.inject.Singleton

interface IAuthenticationRepository {

    suspend fun getAuthentication(): Authentication

    suspend fun updateAuthentication(authentication: Authentication)
}

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        memoryStorageContainer: IMemoryStorageContainer,
        @CompositeLifespanCleaner
        compositeLifespanCleaner: ILifespanCleaner
    ): IAuthenticationRepository {
        return AuthenticationCombinator(
            authenticationMemoryStorage = AuthenticationMemoryStorage(memoryStorageContainer),
            compositeLifespanCleaner = compositeLifespanCleaner
        )
    }
}
