package com.example.todotasks.domain.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Serializable
data class SettingsApp(
    val groupAndSort: GroupAndSort = GroupAndSort()
)

object SettingsAppSerializer : Serializer<SettingsApp> {

    override val defaultValue: SettingsApp = SettingsApp()

    override suspend fun readFrom(input: InputStream): SettingsApp =
        try {
            Json.decodeFromString<SettingsApp>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: SettingsApp, output: OutputStream) {

        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }
}