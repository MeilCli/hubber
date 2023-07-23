package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences

internal class PreferenceData<T>(
    private val preferenceDataSource: PreferenceDataSource,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) : IData<T> {

    override suspend fun getData(): T {
        return preferenceDataSource.getData(key, defaultValue)
    }

    override suspend fun updateData(transform: suspend (T) -> T) {
        preferenceDataSource.updateData(key, defaultValue, transform)
    }
}
