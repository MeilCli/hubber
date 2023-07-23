package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences

internal class PickPreferenceData<T>(
    private val preferenceData: PreferenceData,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) : IData<T> {

    override suspend fun getData(): T {
        return preferenceData.getData(key, defaultValue)
    }

    override suspend fun updateData(transform: suspend (T) -> T) {
        preferenceData.updateData(key, defaultValue, transform)
    }
}
