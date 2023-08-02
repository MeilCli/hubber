package net.meilcli.hubber.core.data.paiging.segmentation.multiple

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequestParameter
import net.meilcli.hubber.core.data.paiging.segmentation.single.SingleSegmentationPagingList

sealed class MultipleSegmentationPagingRequestParameter<
    TPagingElement : IPagingElement,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > : IPagingRequestParameter {

    class Initial<
        TPagingElement : IPagingElement,
        TPagingListSegment : IPagingListSegment<TPagingElement>
        > : MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    class Refresh<
        TPagingElement : IPagingElement,
        TPagingListSegment : IPagingListSegment<TPagingElement>
        > : MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    data class Previous<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
        internal val pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        internal val segments: List<TPagingListSegment>
    ) : MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    data class Between<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
        internal val pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        internal val segments: List<TPagingListSegment>,
        internal val startEdgeSegment: TPagingListSegment,
        internal val endEdgeSegment: TPagingListSegment
    ) : MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()

    data class Next<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
        internal val pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        internal val segments: List<TPagingListSegment>
    ) : MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>()
}
