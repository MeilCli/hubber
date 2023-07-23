package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.Preferences

interface IPreferenceData : IData<Preferences> {

    fun <T> pick(key: Preferences.Key<T>, defaultValue: T): IData<T>
}
