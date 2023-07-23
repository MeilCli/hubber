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
class PickPreferenceDataTest {

    private val preferenceKey = intPreferencesKey("test_key")
    private val coroutineDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun testInitialData() = runTest(coroutineDispatcher) {
        val defaultValue = 1
        val data = PickPreferenceData(
            PreferenceData(
                jetpackDataStoreCreator = {
                    PreferenceDataStoreFactory.create(
                        produceFile = { File(temporaryFolder.root, "data.preferences_pb") },
                        scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                    )
                }
            ),
            preferenceKey,
            defaultValue
        )

        val actual = data.getData()

        Truth.assertThat(actual).isEqualTo(defaultValue)
    }

    @Test
    fun testUpdateData() = runTest(UnconfinedTestDispatcher()) {
        val defaultValue = 1
        val data = PickPreferenceData(
            PreferenceData(
                jetpackDataStoreCreator = {
                    PreferenceDataStoreFactory.create(
                        produceFile = { File(temporaryFolder.root, "data.preferences_pb") },
                        scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                    )
                }
            ),
            preferenceKey,
            defaultValue
        )

        data.updateData { 100 }
        val actual = data.getData()

        Truth.assertThat(actual).isEqualTo(100)
    }
}
