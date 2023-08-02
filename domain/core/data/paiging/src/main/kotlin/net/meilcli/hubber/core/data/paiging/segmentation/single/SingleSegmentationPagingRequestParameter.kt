package net.meilcli.hubber.core.data.paiging.segmentation.single

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequestParameter

sealed class SingleSegmentationPagingRequestParameter<
    TPagingElement : IPagingElement,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > : IPagingRequestParameter {

    class Initial<
        TPagingElement : IPagingElement,
        TPagingListSegment : IPagingListSegment<TPagingElement>
        > : SingleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    class Refresh<
        TPagingElement : IPagingElement,
        TPagingListSegment : IPagingListSegment<TPagingElement>
        > : SingleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    data class Previous<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
        internal val pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        internal val segment: TPagingListSegment
    ) : SingleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    data class Next<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
        internal val pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        internal val segment: TPagingListSegment
    ) : SingleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()
}
