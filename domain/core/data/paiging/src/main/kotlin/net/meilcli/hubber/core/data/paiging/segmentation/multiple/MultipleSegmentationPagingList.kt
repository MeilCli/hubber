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
    private val segments: List<TPagingListSegment>
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

    sealed class ElementOrRequest<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>> {

        data class Element<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
            val value: TPagingElement
        ) : ElementOrRequest<TPagingElement, TPagingListSegment>()

        data class Request<TPagingElement : IPagingElement, TPagingListSegment : IPagingListSegment<TPagingElement>>(
            val value: MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>
        ) : ElementOrRequest<TPagingElement, TPagingListSegment>()
    }

    fun <TPagingRequest : IPagingRequest> needInitialLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Boolean {
        check(segments.isEmpty()) { "MultipleSegmentationPagingList must make instant from MultipleSegmentationPagingList.factory()" }

        return with(pagingRequestProvider) { segments[0].needInitialLoad() }
    }

    fun initialRequestParameter(): MultipleSegmentationPagingRequestParameter.Initial<TPagingElement, TPagingListSegment> {
        return MultipleSegmentationPagingRequestParameter.Initial()
    }

    fun refreshRequestParameter(): MultipleSegmentationPagingRequestParameter.Refresh<TPagingElement, TPagingListSegment> {
        return MultipleSegmentationPagingRequestParameter.Refresh()
    }

    fun previousRequestParameter(): MultipleSegmentationPagingRequestParameter.Previous<TPagingElement, TPagingListSegment> {
        return MultipleSegmentationPagingRequestParameter.Previous(this, segments)
    }

    fun nextRequestParameter(): MultipleSegmentationPagingRequestParameter.Next<TPagingElement, TPagingListSegment> {
        return MultipleSegmentationPagingRequestParameter.Next(this, segments)
    }

    fun elementOfRequests(): List<ElementOrRequest<TPagingElement, TPagingListSegment>> {
        val result = mutableListOf<ElementOrRequest<TPagingElement, TPagingListSegment>>()

        if (segments.isNotEmpty() && segments.first().reachingStartEdge.not()) {
            result += ElementOrRequest.Request(previousRequestParameter())
        }
        var previousSegment: TPagingListSegment? = null
        for (segment in segments) {
            if (previousSegment != null) {
                result += ElementOrRequest.Request(
                    MultipleSegmentationPagingRequestParameter.Between(
                        pagingList = this,
                        segments = segments,
                        startEdgeSegment = previousSegment,
                        endEdgeSegment = segment
                    )
                )
            }
            for (element in segment) {
                result += ElementOrRequest.Element(element)
            }
            previousSegment = segment
        }
        if (segments.isNotEmpty() && segments.last().reachingEndEdge.not()) {
            result += ElementOrRequest.Request(nextRequestParameter())
        }

        return result
    }
}
