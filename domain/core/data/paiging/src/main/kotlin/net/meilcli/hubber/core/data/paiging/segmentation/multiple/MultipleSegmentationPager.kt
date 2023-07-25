package net.meilcli.hubber.core.data.paiging.segmentation.multiple

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import net.meilcli.hubber.core.data.paiging.IPager
import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest
import net.meilcli.hubber.core.data.paiging.IPagingSource

class MultipleSegmentationPager<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > :
    IPager<
        TPagingElement,
        TPagingRequest,
        TPagingListSegment,
        MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
        > {

    private suspend fun firstLoad(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>,
        forceRefresh: Boolean
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        val segment = pagingList.segments.first()
        val pagingRequest = segment.initialPagingRequest()
        return pagingSource.firstLoad(pagingRequest, forceRefresh)
            .map {
                MultipleSegmentationPagingList(
                    listOf(segment.createNewSegmentWithInitial(it.elements, it.reachingStartEdge, it.reachingEndEdge))
                )
            }
    }

    override suspend fun loadInitial(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, false)
    }

    override suspend fun loadRefresh(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, true)
    }

    override suspend fun loadPrevious(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        val segment = pagingList.segments.first()
        if (segment.canPrevious().not()) {
            return emptyFlow()
        }

        val pagingRequest = segment.previousPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result ->
                MultipleSegmentationPagingList(
                    listOf(segment.createNewSegmentWithPrevious(result, pagingRequest)) + pagingList.segments.drop(1)
                )
            }
    }

    override suspend fun loadNext(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        val segment = pagingList.segments.last()
        if (segment.canNext().not()) {
            return emptyFlow()
        }

        val pagingRequest = segment.nextPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result ->
                MultipleSegmentationPagingList(
                    pagingList.segments.dropLast(1) + listOf(segment.createNewSegmentWithNext(result, pagingRequest))
                )
            }
    }

    suspend fun loadPreviousBetweenSegments(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>,
        startEdgeSegment: TPagingListSegment,
        endEdgeSegment: TPagingListSegment
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        if (endEdgeSegment.canPrevious().not()) {
            return emptyFlow()
        }

        val startSegments = pagingList.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingList.segments.takeLastWhile { it != endEdgeSegment }

        val pagingRequest = endEdgeSegment.previousPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result -> endEdgeSegment.createNewSegmentWithPrevious(result, pagingRequest) }
            .map { newSegment ->
                if (startEdgeSegment.needsConcat(newSegment)) {
                    MultipleSegmentationPagingList(
                        startSegments + listOf(startEdgeSegment.concatWithNextSegment(newSegment)) + endSegments
                    )
                } else {
                    MultipleSegmentationPagingList(
                        startSegments + listOf(startEdgeSegment) + listOf(newSegment) + endSegments
                    )
                }
            }
    }

    suspend fun loadNextBetweenSegments(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>,
        startEdgeSegment: TPagingListSegment,
        endEdgeSegment: TPagingListSegment
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        if (startEdgeSegment.canNext().not()) {
            return emptyFlow()
        }

        val startSegments = pagingList.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingList.segments.takeLastWhile { it != endEdgeSegment }

        val pagingRequest = startEdgeSegment.nextPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result -> startEdgeSegment.createNewSegmentWithNext(result, pagingRequest) }
            .map { newSegment ->
                if (newSegment.needsConcat(endEdgeSegment)) {
                    MultipleSegmentationPagingList(
                        startSegments + listOf(newSegment.concatWithNextSegment(endEdgeSegment)) + endSegments
                    )
                } else {
                    MultipleSegmentationPagingList(
                        startSegments + listOf(newSegment) + listOf(endEdgeSegment) + endSegments
                    )
                }
            }
    }
}
