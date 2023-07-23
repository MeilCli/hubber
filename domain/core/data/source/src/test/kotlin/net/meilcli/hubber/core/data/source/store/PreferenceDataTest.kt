package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.intPreferencesKey
import com.google.common.truth.Truth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@ExperimentalCoroutinesApi
class PreferenceDataTest {

    private val preferenceKey = intPreferencesKey("test_key")
    private val coroutineDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun testPick_returnSameInstance() = runTest(coroutineDispatcher) {
        val data = PreferenceData(
            jetpackDataStoreCreator = {
                PreferenceDataStoreFactory.create(
                    produceFile = { File(temporaryFolder.root, "data.preferences_pb") },
                    scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                )
            }
        )

        val pick1 = data.pick(preferenceKey, 1)
        val pick2 = data.pick(preferenceKey, 1)

        Truth.assertThat(pick1).isEqualTo(pick2)
    }

    @Test
    fun testUpdateData() = runTest(UnconfinedTestDispatcher()) {
        val data = PreferenceData(
            jetpackDataStoreCreator = {
                PreferenceDataStoreFactory.create(
                    produceFile = { File(temporaryFolder.root, "data.preferences_pb") },
                    scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                )
            }
        )

        data.updateData {
            it.toMutablePreferences()
                .apply {
                    this[preferenceKey] = 100
                }
                .toPreferences()
        }
        val actual = data.getData()

        Truth.assertThat(actual[preferenceKey]).isEqualTo(100)
    }
}
