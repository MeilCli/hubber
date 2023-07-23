package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.DataStoreFactory
import com.google.common.truth.Truth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@ExperimentalCoroutinesApi
class EntityDataTest {

    @Serializable
    private data class TestEntity(
        @SerialName("value")
        val value: String = ""
    )

    private val coroutineDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun testInitialData() = runTest(coroutineDispatcher) {
        val defaultValue = TestEntity("value")
        val data = EntityData(
            jetpackDataStoreCreator = {
                DataStoreFactory.create(
                    serializer = JsonSerializer(defaultValue, TestEntity.serializer()),
                    produceFile = { File(temporaryFolder.root, "data.json") },
                    scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                )
            }
        )

        val actual = data.getData()

        Truth.assertThat(actual).isEqualTo(defaultValue)
    }

    @Test
    fun testUpdateData() = runTest(UnconfinedTestDispatcher()) {
        val defaultValue = TestEntity("value")
        val data = EntityData(
            jetpackDataStoreCreator = {
                DataStoreFactory.create(
                    serializer = JsonSerializer(defaultValue, TestEntity.serializer()),
                    produceFile = { File(temporaryFolder.root, "data.json") },
                    scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
                )
            }
        )

        data.updateData { TestEntity("updated") }
        val actual = data.getData()

        Truth.assertThat(actual).isEqualTo(TestEntity("updated"))
    }
}
