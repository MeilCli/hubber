package net.meilcli.hubber.core.data.paiging.segmentation.multiple

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

class MultipleSegmentationPager<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    >(
    private val pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
    private val pagingListSegmentConnector: IPagingListSegmentConnector<TPagingElement, TPagingResult, TPagingListSegment>
) :
    IPager<
        TPagingElement,
        TPagingRequest,
        TPagingResult,
        TPagingListSegment,
        MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>
        > {

    private suspend fun firstLoad(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        forceRefresh: Boolean
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val pagingRequest = pagingRequestProvider.initialPagingRequest
        return pagingSource.load(pagingRequest, forceRefresh)
            .map { result ->
                with(pagingListSegmentConnector) {
                    MultipleSegmentationPagingList(listOf(createNewSegment(result)))
                }
            }
    }

    override suspend fun loadInitial(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingSource, forceRefresh = false)
    }

    override suspend fun loadRefresh(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingSource, forceRefresh = true)
    }

    override suspend fun loadPrevious(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingSource)
        }

        val segment = pagingList.segments.first()
        if (with(pagingRequestProvider) { segment.canPrevious().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.previousPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    // ToDo: check concat segment or isolated segment
                    MultipleSegmentationPagingList(
                        listOf(segment.createNewSegmentWithPrevious(result)) + pagingList.segments.drop(1)
                    )
                }
            }
    }

    override suspend fun loadNext(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingSource)
        }

        val segment = pagingList.segments.last()
        if (with(pagingRequestProvider) { segment.canNext().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.nextPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    // ToDo: check concat segment or isolated segment
                    MultipleSegmentationPagingList(
                        pagingList.segments.dropLast(1) + listOf(segment.createNewSegmentWithNext(result))
                    )
                }
            }
    }

    suspend fun loadPreviousBetweenSegments(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        startEdgeSegment: TPagingListSegment,
        endEdgeSegment: TPagingListSegment
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (with(pagingRequestProvider) { endEdgeSegment.canPrevious().not() }) {
            return emptyFlow()
        }

        val startSegments = pagingList.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingList.segments.takeLastWhile { it != endEdgeSegment }

        val pagingRequest = with(pagingRequestProvider) { endEdgeSegment.previousPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    endEdgeSegment.createNewSegmentWithPrevious(result)
                }
            }
            .map { newSegment ->
                with(pagingListSegmentConnector) {
                    if (startEdgeSegment.needsConcat(newSegment)) {
                        // ToDo: check needConcat with endSegment
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
    }

    suspend fun loadNextBetweenSegments(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>,
        startEdgeSegment: TPagingListSegment,
        endEdgeSegment: TPagingListSegment
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (with(pagingRequestProvider) { startEdgeSegment.canNext().not() }) {
            return emptyFlow()
        }

        val startSegments = pagingList.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingList.segments.takeLastWhile { it != endEdgeSegment }

        val pagingRequest = with(pagingRequestProvider) { startEdgeSegment.nextPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    startEdgeSegment.createNewSegmentWithNext(result)
                }
            }
            .map { newSegment ->
                with(pagingListSegmentConnector) {
                    if (newSegment.needsConcat(endEdgeSegment)) {
                        // ToDo: check needConcat with endSegment
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
}
