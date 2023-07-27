package net.meilcli.hubber.core.data.paiging

interface IPagingResult<TPagingElement : IPagingElement> {

    val elements: List<TPagingElement>
    val reachingStartEdge: Boolean
    val reachingEndEdge: Boolean
}
