package net.meilcli.hubber.core.data.paiging

interface IPagingResult<out TPagingElement : IPagingElement> {

    val elements: List<TPagingElement>
    val reachingStartEdge: Boolean
    val reachingEndEdge: Boolean
}
