package net.meilcli.hubber.core.data.paiging.segmentation.single

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import net.meilcli.hubber.core.data.paiging.IPager
import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment
import net.meilcli.hubber.core.data.paiging.IPagingListSegmentConnector
import net.meilcli.hubber.core.data.paiging.IPagingRequest
import net.meilcli.hubber.core.data.paiging.IPagingRequestProvider
import net.meilcli.hubber.core.data.paiging.IPagingSource

class SingleSegmentationPager<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    >(
    private val pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
    private val pagingListSegmentConnector: IPagingListSegmentConnector<TPagingElement, TPagingRequest, TPagingListSegment>
) :
    IPager<
        TPagingElement,
        TPagingRequest,
        TPagingListSegment,
        SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
        > {

    private suspend fun firstLoad(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        forceRefresh: Boolean
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val segment = pagingList.segment
        val pagingRequest = with(pagingRequestProvider) { segment.initialPagingRequest() }
        return pagingSource.firstLoad(pagingRequest, forceRefresh)
            .map {
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(
                        segment.createNewSegmentWithInitial(it.elements, it.reachingStartEdge, it.reachingEndEdge)
                    )
                }
            }
    }

    override suspend fun loadInitial(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, false)
    }

    override suspend fun loadRefresh(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingSource, pagingList, true)
    }

    override suspend fun loadPrevious(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingSource, pagingList)
        }

        val segment = pagingList.segment
        if (with(pagingRequestProvider) { segment.canPrevious().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.previousPagingRequest() }
        return pagingSource.load(pagingRequest)
            .map { result ->
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(
                        segment.createNewSegmentWithPrevious(result, pagingRequest)
                    )
                }
            }
    }

    override suspend fun loadNext(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingSource, pagingList)
        }

        val segment = pagingList.segment
        if (with(pagingRequestProvider) { segment.canNext().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.nextPagingRequest() }
        return pagingSource.load(pagingRequest)
            .map { result ->
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(
                        segment.createNewSegmentWithNext(result, pagingRequest)
                    )
                }
            }
    }
}
