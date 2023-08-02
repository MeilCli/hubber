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
    private val segment: TPagingListSegment
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

    sealed class ElementOrRequest<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> {

        data class Element<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
            val value: TPagingElement
        ) : ElementOrRequest<TPagingElement, TPagingListSegment>()

        data class Request<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
            val value: SingleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>
        ) : ElementOrRequest<TPagingElement, TPagingListSegment>()
    }

    fun <TPagingRequest : IPagingRequest> needInitialLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Boolean {
        return with(pagingRequestProvider) { segment.needInitialLoad() }
    }

    fun initialRequestParameter(): SingleSegmentationPagingRequestParameter.Initial<TPagingElement, TPagingListSegment> {
        return SingleSegmentationPagingRequestParameter.Initial()
    }

    fun refreshRequestParameter(): SingleSegmentationPagingRequestParameter.Refresh<TPagingElement, TPagingListSegment> {
        return SingleSegmentationPagingRequestParameter.Refresh()
    }

    fun previousRequestParameter(): SingleSegmentationPagingRequestParameter.Previous<TPagingElement, TPagingListSegment> {
        return SingleSegmentationPagingRequestParameter.Previous(this, segment)
    }

    fun nextRequestParameter(): SingleSegmentationPagingRequestParameter.Next<TPagingElement, TPagingListSegment> {
        return SingleSegmentationPagingRequestParameter.Next(this, segment)
    }

    fun elementOfRequests(): List<ElementOrRequest<TPagingElement, TPagingListSegment>> {
        val result = mutableListOf<ElementOrRequest<TPagingElement, TPagingListSegment>>()

        if (segment.reachingStartEdge.not()) {
            result += ElementOrRequest.Request(previousRequestParameter())
        }
        for (element in segment) {
            result += ElementOrRequest.Element(element)
        }
        if (segment.reachingEndEdge.not()) {
            result += ElementOrRequest.Request(nextRequestParameter())
        }

        return result
    }
}
