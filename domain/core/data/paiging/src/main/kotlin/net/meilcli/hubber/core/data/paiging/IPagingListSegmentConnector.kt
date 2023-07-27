package net.meilcli.hubber.core.data.paiging

interface IPagingListSegmentConnector<
    TPagingElement : IPagingElement,
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > {

    fun createNewSegment(
        result: TPagingResult
    ): TPagingListSegment

    fun TPagingListSegment.createNewSegmentWithPrevious(
        result: TPagingResult
    ): TPagingListSegment

    fun TPagingListSegment.createNewSegmentWithNext(
        result: TPagingResult
    ): TPagingListSegment

    fun TPagingListSegment.needsConcat(nextSegment: TPagingListSegment): Boolean

    fun TPagingListSegment.concatWithNextSegment(
        nextSegment: TPagingListSegment
    ): TPagingListSegment
}
