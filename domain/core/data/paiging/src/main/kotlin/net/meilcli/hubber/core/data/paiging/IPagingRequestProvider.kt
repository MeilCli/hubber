package net.meilcli.hubber.core.data.paiging

interface IPagingRequestProvider<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > {

    fun TPagingListSegment.needInitialLoad(): Boolean

    fun TPagingListSegment.initialPagingRequest(): TPagingRequest

    fun TPagingListSegment.canPrevious(): Boolean

    fun TPagingListSegment.previousPagingRequest(): TPagingRequest

    fun TPagingListSegment.canNext(): Boolean

    fun TPagingListSegment.nextPagingRequest(): TPagingRequest
}
