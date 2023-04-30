package net.meilcli.hubber.core.data.main.internal.cache

import net.meilcli.hubber.core.data.main.ITestDataSource
import javax.inject.Inject

internal class TestCache @Inject constructor() : ITestDataSource {

    override fun hello(): String {
        return "hello world from cache"
    }
}
