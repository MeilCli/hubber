package net.meilcli.hubber.authentication.data.main.internal.combine

import net.meilcli.hubber.authentication.data.main.IAuthenticationRepository
import net.meilcli.hubber.authentication.data.main.entity.Authentication
import net.meilcli.hubber.authentication.data.main.internal.memory.AuthenticationMemoryStorage
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner

class AuthenticationCombinator(
    private val authenticationMemoryStorage: AuthenticationMemoryStorage,
    private val compositeLifespanCleaner: ILifespanCleaner
) : IAuthenticationRepository {

    override suspend fun getAuthentication(): Authentication {
        return authenticationMemoryStorage.getAuthentication()
    }

    override suspend fun updateAuthentication(authentication: Authentication) {
        if (authentication == Authentication.Never) {
            compositeLifespanCleaner.clearByLogout()
        }
        authenticationMemoryStorage.updateAuthentication(authentication)
    }
}
