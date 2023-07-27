package net.meilcli.hubber.core.data.paiging.segmentation.single

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingList
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest
import net.meilcli.hubber.core.data.paiging.IPagingRequestProvider

class SingleSegmentationPagingList<
    TPagingElement : IPagingElement,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > internal constructor(
    internal val segment: TPagingListSegment
) : IPagingList<TPagingElement, TPagingListSegment>, List<TPagingElement> by segment {

    companion object {

        fun <
            TPagingElement : IPagingElement,
            TPagingListSegment : IPagingListSegment<TPagingElement>
            > factory(): Factory<TPagingElement, TPagingListSegment> {
            return Factory()
        }
    }

    class Factory<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> :
        IPagingList.IPagingListFactory<
            TPagingElement,
            TPagingListSegment,
            SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
            > {

        override fun createEmpty(
            emptySegment: TPagingListSegment
        ): SingleSegmentationPagingList<TPagingElement, TPagingListSegment> {
            return SingleSegmentationPagingList(emptySegment)
        }
    }

    fun <TPagingRequest : IPagingRequest> needInitialLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Boolean {
        return with(pagingRequestProvider) { segment.needInitialLoad() }
    }
}
