@file:OptIn(ExperimentalCoroutinesApi::class)

package net.meilcli.hubber.core.data.source.memory

import com.google.common.truth.Truth
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import net.meilcli.hubber.core.data.source.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.MemoryStorageCleaner
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(Enclosed::class)
class MemoryStorageContainerTest {

    class DiModule {

        @Singleton
        @Component(modules = [MemoryStorageContainerModule::class])
        interface ITestComponent {

            @Component.Factory
            interface IFactory {

                fun create(): ITestComponent
            }

            fun inject(module: DiModule)
        }

        @Inject
        lateinit var memoryStorageContainer: IMemoryStorageContainer

        @MemoryStorageCleaner
        @Inject
        lateinit var memoryStorageCleaner: ILifespanCleaner

        @Before
        fun setup() {
            DaggerMemoryStorageContainerTest_DiModule_ITestComponent.factory()
                .create()
                .inject(this)
        }

        @Test
        fun testContainerAndCleanerAreEqualsInstance() {
            Truth.assertThat(memoryStorageContainer).isEqualTo(memoryStorageCleaner)
        }
    }

    class LifeSpan : IMemoryStorageOwner {

        object OtherOwner : IMemoryStorageOwner

        @Test
        fun testForever() {
            val memoryStorageContainer = MemoryStorageContainer()

            val memoryStorage1 = memoryStorageContainer.lifeSpan(Lifespan.Forever, this)
            val memoryStorage2 = memoryStorageContainer.lifeSpan(Lifespan.Forever, this)
            val memoryStorage3 = memoryStorageContainer.lifeSpan(Lifespan.Forever, OtherOwner)
            val memoryStorage4 = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this)
            val memoryStorage5 = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this)

            Truth.assertThat(memoryStorage1).isEqualTo(memoryStorage2)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage3)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage4)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage5)
        }

        @Test
        fun testUntilLogout() {
            val memoryStorageContainer = MemoryStorageContainer()

            val memoryStorage1 = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this)
            val memoryStorage2 = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this)
            val memoryStorage3 = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, OtherOwner)
            val memoryStorage4 = memoryStorageContainer.lifeSpan(Lifespan.Forever, this)
            val memoryStorage5 = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this)

            Truth.assertThat(memoryStorage1).isEqualTo(memoryStorage2)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage3)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage4)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage5)
        }

        @Test
        fun testUntilTaskKill() {
            val memoryStorageContainer = MemoryStorageContainer()

            val memoryStorage1 = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this)
            val memoryStorage2 = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this)
            val memoryStorage3 = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, OtherOwner)
            val memoryStorage4 = memoryStorageContainer.lifeSpan(Lifespan.Forever, this)
            val memoryStorage5 = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this)

            Truth.assertThat(memoryStorage1).isEqualTo(memoryStorage2)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage3)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage4)
            Truth.assertThat(memoryStorage1).isNotEqualTo(memoryStorage5)
        }
    }

    class ClearByLogout : IMemoryStorageOwner {

        @Test
        fun testForever() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.Forever, this@ClearByLogout)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByLogout()

            Truth.assertThat(memory.getData()).isEqualTo("updated")
        }

        @Test
        fun testUntilLogout() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this@ClearByLogout)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByLogout()

            Truth.assertThat(memory.getData()).isEqualTo("value")
        }

        @Test
        fun testUntilTaskKill() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this@ClearByLogout)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByLogout()

            Truth.assertThat(memory.getData()).isEqualTo("updated")
        }
    }

    class ClearByTaskKill : IMemoryStorageOwner {

        @Test
        fun testForever() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.Forever, this@ClearByTaskKill)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByTaskKill()

            Truth.assertThat(memory.getData()).isEqualTo("updated")
        }

        @Test
        fun testUntilLogout() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.UntilLogout, this@ClearByTaskKill)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByTaskKill()

            Truth.assertThat(memory.getData()).isEqualTo("updated")
        }

        @Test
        fun testUntilTaskKill() = runTest(UnconfinedTestDispatcher()) {
            val memoryStorageContainer = MemoryStorageContainer()
            val memoryStorage = memoryStorageContainer.lifeSpan(Lifespan.UntilTaskKill, this@ClearByTaskKill)
            val memory = memoryStorage.memory("a", "value")
            memory.updateData { "updated" }

            memoryStorageContainer.clearByTaskKill()

            Truth.assertThat(memory.getData()).isEqualTo("value")
        }
    }
}
