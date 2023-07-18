package net.meilcli.hubber.core.data.source.file

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.meilcli.hubber.core.data.source.cleaner.FileSystemCleaner
import net.meilcli.hubber.core.data.source.cleaner.ILifespanCleaner
import net.meilcli.hubber.core.data.source.Lifespan
import javax.inject.Singleton

interface IFileSystemContainer {

    fun lifeSpan(lifespan: Lifespan): IFileSystem
}

@Module
@InstallIn(SingletonComponent::class)
object FileSystemContainerModule {

    @Provides
    @Singleton
    fun provideFileSystemContainer(
        @ApplicationContext
        context: Context
    ): IFileSystemContainer {
        return FileSystemContainer(context)
    }

    @FileSystemCleaner
    @Provides
    @Singleton
    fun provideFileSystemContainerCleaner(
        fileSystemContainer: IFileSystemContainer
    ): ILifespanCleaner {
        return fileSystemContainer as FileSystemContainer
    }
}
