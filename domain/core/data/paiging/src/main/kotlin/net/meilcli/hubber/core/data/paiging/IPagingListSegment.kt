package net.meilcli.hubber.core.data.paiging

@Suppress("detekt.TooManyFunctions")
interface IPagingListSegment<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > : List<TPagingElement> {

    interface IPagingListSegmentFactory<
        TPagingElement : IPagingElement,
        TPagingRequest : IPagingRequest,
        TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
        > {

        fun createEmpty(initialPage: Int, countPerPage: Int): TPagingListSegment
    }

    val countPerPage: Int
    val reachingStartEdge: Boolean
    val reachingEndEdge: Boolean

    fun needInitialLoad(): Boolean

    fun initialPagingRequest(): TPagingRequest

    fun createNewSegmentWithInitial(
        initialElements: List<TPagingElement>,
        reachingStartEdge: Boolean,
        reachingEndEdge: Boolean
    ): TPagingListSegment

    fun canPrevious(): Boolean

    fun previousPagingRequest(): TPagingRequest

    fun createNewSegmentWithPrevious(
        previousElements: List<TPagingElement>,
        pagingRequest: TPagingRequest
    ): TPagingListSegment

    fun canNext(): Boolean

    fun nextPagingRequest(): TPagingRequest

    fun createNewSegmentWithNext(
        nextElements: List<TPagingElement>,
        pagingRequest: TPagingRequest
    ): TPagingListSegment

    fun needsConcat(nextSegment: TPagingListSegment): Boolean

    fun concatWithNextSegment(
        nextSegment: TPagingListSegment
    ): TPagingListSegment
}
