package net.meilcli.hubber.core.data.source.store

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal class JsonSerializer<T>(
    override val defaultValue: T,
    private val serializer: KSerializer<T>
) : Serializer<T> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun readFrom(input: InputStream): T {
        return json.decodeFromString(serializer, input.readBytes().decodeToString())
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: T, output: OutputStream) {
        output.write(json.encodeToString(serializer, t).encodeToByteArray())
    }
}
