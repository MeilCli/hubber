package net.meilcli.hubber.core.data.paiging

interface IPagingListSegment<TPagingElement : IPagingElement> : List<TPagingElement> {

    interface IPagingListSegmentFactory<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> {

        fun createEmpty(): TPagingListSegment
    }

    val reachingStartEdge: Boolean
    val reachingEndEdge: Boolean
}
