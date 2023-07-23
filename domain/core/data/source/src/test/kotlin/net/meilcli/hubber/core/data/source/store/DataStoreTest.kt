@file:OptIn(ExperimentalCoroutinesApi::class)

package net.meilcli.hubber.core.data.source.store

import androidx.datastore.preferences.core.intPreferencesKey
import com.google.common.truth.Truth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.meilcli.hubber.core.data.source.ICoroutineDispatcherProvider
import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class DataStoreTest {

    @Serializable
    private data class TestEntity(
        @SerialName("value")
        val value: String = ""
    )

    private inner class TestLocalFileDirectoryProvider : ILocalFileDirectoryProvider {

        override fun provideFileDirectory(): File {
            return temporaryFolder.root
        }
    }

    private inner class TestCoroutineDispatcherProvider : ICoroutineDispatcherProvider {

        override fun provideIoDispatcher(): CoroutineDispatcher {
            return coroutineDispatcher
        }
    }

    private val coroutineDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun testEntityData_returnSameInstance() = runTest(coroutineDispatcher) {
        val dataStore = DataStore(
            localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            parentDirectoryName = "test_life_span"
        )

        val data1 = dataStore.entityData("test_data", TestEntity())
        val data2 = dataStore.entityData("test_data", TestEntity())

        Truth.assertThat(data1).isEqualTo(data2)
    }

    @Test
    fun testPreferenceData_returnSameInstance() = runTest(coroutineDispatcher) {
        val dataStore = DataStore(
            localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            parentDirectoryName = "test_life_span"
        )

        val data1 = dataStore.preferenceData("test_data")
        val data2 = dataStore.preferenceData("test_data")

        Truth.assertThat(data1).isEqualTo(data2)
    }

    @Test
    fun testClear() = runTest(coroutineDispatcher) {
        val dataStore = DataStore(
            localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            parentDirectoryName = "test_life_span"
        )
        val preferenceKey = intPreferencesKey("test")
        val entityData = dataStore.entityData("test_entity_data", TestEntity())
        val preferenceData = dataStore.preferenceData("test_preference_data").pick(preferenceKey, 1)

        entityData.updateData { TestEntity("updated") }
        preferenceData.updateData { 100 }

        Truth.assertThat(entityData.getData()).isEqualTo(TestEntity("updated"))
        Truth.assertThat(preferenceData.getData()).isEqualTo(100)

        dataStore.clear()

        Truth.assertThat(entityData.getData()).isEqualTo(TestEntity())
        Truth.assertThat(preferenceData.getData()).isEqualTo(1)
    }
}
