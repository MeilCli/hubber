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
import net.meilcli.hubber.core.data.paiging.IPagingResult
import net.meilcli.hubber.core.data.paiging.IPagingSource

class SingleSegmentationPager<
    TPagingElement : IPagingElement,
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    >(
    private val pagingListSegmentConnector: IPagingListSegmentConnector<TPagingElement, TPagingResult, TPagingListSegment>
) :
    IPager<
        TPagingElement,
        TPagingResult,
        TPagingListSegment,
        SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
        > {

    private suspend fun <TPagingRequest : IPagingRequest> firstLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        forceRefresh: Boolean
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val pagingRequest = pagingRequestProvider.initialPagingRequest()
        return pagingSource.load(pagingRequest, forceRefresh)
            .map { result ->
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(createNewSegment(result))
                }
            }
    }

    override suspend fun <TPagingRequest : IPagingRequest> loadInitial(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingRequestProvider, pagingSource, forceRefresh = false)
    }

    override suspend fun <TPagingRequest : IPagingRequest> loadRefresh(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingRequestProvider, pagingSource, forceRefresh = true)
    }

    override suspend fun <TPagingRequest : IPagingRequest> loadPrevious(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingRequestProvider, pagingSource)
        }

        val segment = pagingList.segment
        if (with(pagingRequestProvider) { segment.canPrevious().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.previousPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(
                        segment.createNewSegmentWithPrevious(result)
                    )
                }
            }
    }

    override suspend fun <TPagingRequest : IPagingRequest> loadNext(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: SingleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<SingleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingRequestProvider, pagingSource)
        }

        val segment = pagingList.segment
        if (with(pagingRequestProvider) { segment.canNext().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.nextPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    SingleSegmentationPagingList(
                        segment.createNewSegmentWithNext(result)
                    )
                }
            }
    }
}
