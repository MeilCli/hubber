package net.meilcli.hubber.authentication.data.main.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Authentication: Parcelable {

    @Parcelize
    object Never : Authentication()

    @Parcelize
    object Guest : Authentication()

    @Parcelize
    data class LoginUser(
        val id: String,
        val name: String,
        val iconUrl: String,
        val token: String
    ) : Authentication()
}
