package net.meilcli.hubber.core.data.source.memory

import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MemoryTest {

    @Test
    fun testInitialData() = runTest(UnconfinedTestDispatcher()) {
        val initialData = "initial"
        val memory = Memory(initialData)

        val actual = memory.getData()

        Truth.assertThat(actual).isEqualTo(initialData)
    }

    @Test
    fun testUpdateData() = runTest(UnconfinedTestDispatcher()) {
        val newData = "updated"
        val memory = Memory("initial")

        memory.updateData { newData }
        val actual = memory.getData()

        Truth.assertThat(actual).isEqualTo(newData)
    }
}
