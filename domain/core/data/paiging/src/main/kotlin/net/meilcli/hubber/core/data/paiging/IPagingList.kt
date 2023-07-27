package net.meilcli.hubber.core.data.paiging

interface IPagingList<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> : List<TPagingElement> {

    interface IPagingListFactory<
        TPagingElement : IPagingElement,
        TPagingListSegment : IPagingListSegment<TPagingElement>,
        TPagingList : IPagingList<TPagingElement, TPagingListSegment>
        > {

        fun createEmpty(
            emptySegment: TPagingListSegment
        ): TPagingList
    }
}
