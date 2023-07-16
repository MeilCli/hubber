package net.meilcli.hubber.core.ui.main

import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import kotlinx.parcelize.Parcelize

@Parcelize
private data class NullableSaver<T : Parcelable>(
    val value: T?
) : Parcelable

fun <T : Parcelable> nullableSaver(): Saver<T?, Any> {
    return Saver(
        save = { NullableSaver(it) },
        restore = {
            @Suppress("UNCHECKED_CAST")
            (it as NullableSaver<T>).value
        }
    )
}
