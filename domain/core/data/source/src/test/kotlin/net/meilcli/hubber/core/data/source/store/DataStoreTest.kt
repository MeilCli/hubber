@file:OptIn(ExperimentalCoroutinesApi::class)

package net.meilcli.hubber.core.data.source.store

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
    fun testData_returnSameInstance() = runTest(coroutineDispatcher) {
        val dataStore = DataStore(
            localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            parentDirectoryName = "test_life_span"
        )

        val data1 = dataStore.data("test_data", TestEntity())
        val data2 = dataStore.data("test_data", TestEntity())

        Truth.assertThat(data1).isEqualTo(data2)
    }

    @Test
    fun testClear() = runTest(coroutineDispatcher) {
        val dataStore = DataStore(
            localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
            coroutineDispatcherProvider = TestCoroutineDispatcherProvider(),
            parentDirectoryName = "test_life_span"
        )
        val data = dataStore.data("test_data", TestEntity())

        data.updateData { TestEntity("updated") }

        Truth.assertThat(data.getData()).isEqualTo(TestEntity("updated"))

        dataStore.clear()

        Truth.assertThat(data.getData()).isEqualTo(TestEntity())
    }
}
