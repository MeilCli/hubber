package net.meilcli.hubber.core.data.paiging

interface IPagingListSegment<out TPagingElement : IPagingElement> : List<TPagingElement> {

    interface IPagingListSegmentFactory<out TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> {

        fun createEmpty(): TPagingListSegment
    }

    val reachingStartEdge: Boolean
    val reachingEndEdge: Boolean
}
