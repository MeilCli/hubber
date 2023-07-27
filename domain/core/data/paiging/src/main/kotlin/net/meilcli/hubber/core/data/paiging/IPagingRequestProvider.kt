package net.meilcli.hubber.core.data.paiging

interface IPagingRequestProvider<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > {

    val initialPagingRequest: TPagingRequest

    fun TPagingListSegment.needInitialLoad(): Boolean

    fun TPagingListSegment.canPrevious(): Boolean

    fun TPagingListSegment.previousPagingRequest(): TPagingRequest

    fun TPagingListSegment.canNext(): Boolean

    fun TPagingListSegment.nextPagingRequest(): TPagingRequest
}
