package net.meilcli.hubber.core.data.paiging.segmentation.multiple

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingList
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest

class MultipleSegmentationPagingList<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > internal constructor(
    // ToDo: ここをinternalにしても大丈夫なようにsegment間の読み込みをできるようにしたい
    val segments: List<TPagingListSegment>
) : IPagingList<TPagingElement, TPagingRequest, TPagingListSegment>, List<TPagingElement> by segments.flatten() {

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
            MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
            > {

        override fun createEmpty(
            emptySegment: TPagingListSegment
        ): MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment> {
            return MultipleSegmentationPagingList(listOf(emptySegment))
        }
    }

    override fun needInitialLoad(): Boolean {
        check(segments.isEmpty()) { "MultipleSegmentationPagingList must make instant from MultipleSegmentationPagingList.factory()" }

        return segments[0].needInitialLoad()
    }
}
