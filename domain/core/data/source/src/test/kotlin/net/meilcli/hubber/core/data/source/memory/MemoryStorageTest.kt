package net.meilcli.hubber.core.data.source.memory

import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MemoryStorageTest {

    @Test
    fun testMemory_returnSameInstance() {
        val memoryStorage = MemoryStorage()

        val memory1 = memoryStorage.memory("a", "value")
        val memory2 = memoryStorage.memory("a", "value")

        Truth.assertThat(memory1).isEqualTo(memory2)
    }

    @Test
    fun testClear() = runTest(UnconfinedTestDispatcher()) {
        val memoryStorage = MemoryStorage()
        val memory = memoryStorage.memory("a", "value")

        memory.updateData { "updated" }

        Truth.assertThat(memory.getData()).isEqualTo("updated")

        memoryStorage.clear()

        Truth.assertThat(memory.getData()).isEqualTo("value")
    }
}
