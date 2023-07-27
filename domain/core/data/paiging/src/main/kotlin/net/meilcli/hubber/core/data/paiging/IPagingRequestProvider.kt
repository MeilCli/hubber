package net.meilcli.hubber.core.data.paiging

interface IPagingRequestProvider<
    out TPagingElement : IPagingElement,
    out TPagingRequest : IPagingRequest,
    in TPagingListSegment : IPagingListSegment<TPagingElement>
    > {

    fun TPagingListSegment.needInitialLoad(): Boolean

    fun initialPagingRequest(): TPagingRequest

    fun TPagingListSegment.canPrevious(): Boolean

    fun TPagingListSegment.previousPagingRequest(): TPagingRequest

    fun TPagingListSegment.canNext(): Boolean

    fun TPagingListSegment.nextPagingRequest(): TPagingRequest
}
