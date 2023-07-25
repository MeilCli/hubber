package net.meilcli.hubber.core.data.paiging.segmentation.single

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingList
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest

class SingleSegmentationPagingList<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > internal constructor(
    internal val segment: TPagingListSegment
) : IPagingList<TPagingElement, TPagingRequest, TPagingListSegment>, List<TPagingElement> by segment {

    companion object {

        fun <
            TPagingElement : IPagingElement,
            TPagingRequest : IPagingRequest,
            TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
            > factory(): Factory<TPagingElement, TPagingRequest, TPagingListSegment> {
            return Factory()
        }
    }

    class Factory<
        TPagingElement : IPagingElement,
        TPagingRequest : IPagingRequest,
        TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
        > :
        IPagingList.IPagingListFactory<
            TPagingElement,
            TPagingRequest,
            TPagingListSegment,
            SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
            > {

        override fun createEmpty(
            emptySegment: TPagingListSegment
        ): SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment> {
            return SingleSegmentationPagingList(emptySegment)
        }
    }

    override fun needInitialLoad(): Boolean {
        return segment.needInitialLoad()
    }
}
