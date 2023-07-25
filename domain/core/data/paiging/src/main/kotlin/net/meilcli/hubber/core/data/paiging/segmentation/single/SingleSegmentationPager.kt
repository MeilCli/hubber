package net.meilcli.hubber.core.data.paiging.segmentation.single

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import net.meilcli.hubber.core.data.paiging.IPager
import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingRequest
import net.meilcli.hubber.core.data.paiging.IPagingSource

class SingleSegmentationPager<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement, TPagingRequest, TPagingListSegment>
    > :
    IPager<
        TPagingElement,
        TPagingRequest,
        TPagingListSegment,
        SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
        > {

    private suspend fun firstLoad(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>,
        forceRefresh: Boolean
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        val segment = pagingList.segment
        val pagingRequest = segment.initialPagingRequest()
        return pagingSource.firstLoad(pagingRequest, forceRefresh)
            .map {
                SingleSegmentationPagingList(
                    segment.createNewSegmentWithInitial(it.elements, it.reachingStartEdge, it.reachingEndEdge)
                )
            }
    }

    override suspend fun loadInitial(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, false)
    }

    override suspend fun loadRefresh(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, true)
    }

    override suspend fun loadPrevious(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        if (pagingList.needInitialLoad()) {
            return loadInitial(pagingSource, pagingList)
        }

        val segment = pagingList.segment
        if (segment.canPrevious().not()) {
            return emptyFlow()
        }

        val pagingRequest = segment.previousPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result ->
                SingleSegmentationPagingList(
                    segment.createNewSegmentWithPrevious(result, pagingRequest)
                )
            }
    }

    override suspend fun loadNext(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingRequest, TPagingListSegment>> {
        if (pagingList.needInitialLoad()) {
            return loadInitial(pagingSource, pagingList)
        }

        val segment = pagingList.segment
        if (segment.canNext().not()) {
            return emptyFlow()
        }

        val pagingRequest = segment.nextPagingRequest()
        return pagingSource.load(pagingRequest)
            .map { result ->
                SingleSegmentationPagingList(
                    segment.createNewSegmentWithNext(result, pagingRequest)
                )
            }
    }
}
