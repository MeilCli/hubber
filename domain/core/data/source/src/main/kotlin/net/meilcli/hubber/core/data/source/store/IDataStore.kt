package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences
import kotlinx.serialization.KSerializer

interface IDataStore {

    fun <T> entityData(key: String, defaultValue: T, serializer: KSerializer<T>): IData<T>

    fun <T> preferenceData(name: String, key: Preferences.Key<T>, defaultValue: T): IData<T>
}
