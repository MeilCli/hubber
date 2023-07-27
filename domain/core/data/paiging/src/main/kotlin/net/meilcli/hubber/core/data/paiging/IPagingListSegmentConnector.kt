package net.meilcli.hubber.core.data.paiging

interface IPagingListSegmentConnector<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > {

    fun TPagingListSegment.createNewSegmentWithInitial(
        initialElements: List<TPagingElement>,
        reachingStartEdge: Boolean,
        reachingEndEdge: Boolean
    ): TPagingListSegment

    fun TPagingListSegment.createNewSegmentWithPrevious(
        previousElements: List<TPagingElement>,
        pagingRequest: TPagingRequest
    ): TPagingListSegment

    fun TPagingListSegment.createNewSegmentWithNext(
        nextElements: List<TPagingElement>,
        pagingRequest: TPagingRequest
    ): TPagingListSegment

    fun TPagingListSegment.needsConcat(nextSegment: TPagingListSegment): Boolean

    fun TPagingListSegment.concatWithNextSegment(
        nextSegment: TPagingListSegment
    ): TPagingListSegment
}
