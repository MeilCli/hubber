package net.meilcli.hubber.core.data.paiging.segmentation.multiple

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingList
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest
import net.meilcli.hubber.core.data.paiging.IPagingRequestProvider

class MultipleSegmentationPagingList<
    TPagingElement : IPagingElement,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    > internal constructor(
    // ToDo: ここをinternalにしても大丈夫なようにsegment間の読み込みをできるようにしたい
    val segments: List<TPagingListSegment>
) : IPagingList<TPagingElement, TPagingListSegment>, List<TPagingElement> by segments.flatten() {

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
            MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>
            > {

        override fun createEmpty(
            emptySegment: TPagingListSegment
        ): MultipleSegmentationPagingList<TPagingElement, TPagingListSegment> {
            return MultipleSegmentationPagingList(listOf(emptySegment))
        }
    }

    fun <TPagingRequest : IPagingRequest> needInitialLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Boolean {
        check(segments.isEmpty()) { "MultipleSegmentationPagingList must make instant from MultipleSegmentationPagingList.factory()" }

        return with(pagingRequestProvider) { segments[0].needInitialLoad() }
    }
}
