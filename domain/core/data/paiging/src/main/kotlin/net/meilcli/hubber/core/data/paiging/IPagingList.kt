package net.meilcli.hubber.core.data.paiging

interface IPagingList<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > : List<TPagingElement> {

    interface IPagingListFactory<
        TPagingElement : IPagingElement,
        TPagingRequest : IPagingRequest,
        TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>,
        TPagingList : IPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
        > {

        fun createEmpty(
            emptySegment: TPagingListSegment
        ): TPagingList
    }

    fun needInitialLoad(): Boolean
}
