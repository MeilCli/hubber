package net.meilcli.hubber.core.data.source.store

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.meilcli.hubber.core.data.source.ICoroutineDispatcherProvider
import net.meilcli.hubber.core.data.source.ILocalFileDirectoryProvider
import net.meilcli.hubber.core.data.source.Lifespan
import net.meilcli.hubber.core.data.source.cleaner.DataStoreCleaner
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@RunWith(Enclosed::class)
class DataStoreContainerTest {

    @RunWith(RobolectricTestRunner::class)
    class DiModule {

        @Singleton
        @Component(modules = [DataStoreContainerModule::class])
        interface ITestComponent {

            @Component.Factory
            interface IFactory {

                fun create(
                    @BindsInstance
                    @ApplicationContext
                    context: Context
                ): ITestComponent
            }

            fun inject(module: DiModule)
        }

        @Inject
        lateinit var dataStoreContainer: IDataStoreContainer

        @DataStoreCleaner
        @Inject
        lateinit var dataStoreCleaner: ILifespanCleaner

        @Before
        fun setup() {
            DaggerDataStoreContainerTest_DiModule_ITestComponent.factory()
                .create(ApplicationProvider.getApplicationContext())
                .inject(this)
        }

        @Test
        fun testContainerAndCleanerAreEqualsInstance() {
            Truth.assertThat(dataStoreContainer).isEqualTo(dataStoreCleaner)
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class LifeSpan {

        @Test
        fun testForever() {
            val dataStoreContainer = DataStoreContainer(ApplicationProvider.getApplicationContext())

            val dataStore1 = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val dataStore2 = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val dataStore3 = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)
            val dataStore4 = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)

            Truth.assertThat(dataStore1).isEqualTo(dataStore2)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore3)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore4)
        }

        @Test
        fun testUntilLogout() {
            val dataStoreContainer = DataStoreContainer(ApplicationProvider.getApplicationContext())

            val dataStore1 = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)
            val dataStore2 = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)
            val dataStore3 = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val dataStore4 = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)

            Truth.assertThat(dataStore1).isEqualTo(dataStore2)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore3)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore4)
        }

        @Test
        fun testUntilTaskKill() {
            val dataStoreContainer = DataStoreContainer(ApplicationProvider.getApplicationContext())

            val dataStore1 = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)
            val dataStore2 = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)
            val dataStore3 = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val dataStore4 = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)

            Truth.assertThat(dataStore1).isEqualTo(dataStore2)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore3)
            Truth.assertThat(dataStore1).isNotEqualTo(dataStore4)
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class ClearByLogout {

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
        fun testForever() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByLogout()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("updated"))
        }

        @Test
        fun testUntilLogout() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByLogout()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("value"))
        }

        @Test
        fun testUntilTaskKill() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByLogout()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("updated"))
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class ClearByTaskKill {

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
        fun testForever() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.Forever)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByTaskKill()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("updated"))
        }

        @Test
        fun testUntilLogout() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.UntilLogout)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByTaskKill()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("updated"))
        }

        @Test
        fun testUntilTaskKill() = runTest(coroutineDispatcher) {
            val dataStoreContainer = DataStoreContainer(
                localFileDirectoryProvider = TestLocalFileDirectoryProvider(),
                coroutineDispatcherProvider = TestCoroutineDispatcherProvider()
            )
            val dataStore = dataStoreContainer.lifeSpan(Lifespan.UntilTaskKill)
            val data = dataStore.data("a", TestEntity("value"))
            data.updateData { TestEntity("updated") }

            dataStoreContainer.clearByTaskKill()

            Truth.assertThat(data.getData()).isEqualTo(TestEntity("value"))
        }
    }
}
