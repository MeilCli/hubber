package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import kotlinx.serialization.KSerializer

interface IDataStore {

    fun <T> entityData(
        key: String,
        defaultValue: T,
        serializer: KSerializer<T>,
        dataMigrations: List<DataMigration<T>> = emptyList()
    ): IData<T>

    fun preferenceData(
        name: String,
        dataMigrations: List<DataMigration<Preferences>> = emptyList()
    ): IPreferenceData
}
