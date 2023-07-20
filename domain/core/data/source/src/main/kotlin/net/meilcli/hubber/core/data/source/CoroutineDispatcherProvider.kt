package net.meilcli.hubber.core.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// This interface is for testing usage
internal interface ICoroutineDispatcherProvider {

    fun provideIoDispatcher(): CoroutineDispatcher
}

internal class DefaultCoroutineDispatcherProvider : ICoroutineDispatcherProvider {

    override fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
