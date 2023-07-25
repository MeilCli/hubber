package net.meilcli.hubber.core.data.paiging

interface IPagingElement {

    fun isSameTo(other: IPagingElement): Boolean
}
