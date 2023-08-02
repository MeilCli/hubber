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
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>
    >(
    private val pagingListSegmentConnector: IPagingListSegmentConnector<TPagingElement, TPagingResult, TPagingListSegment>
) :
    IPager<
        TPagingElement,
        MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>,
        TPagingResult,
        TPagingListSegment,
        MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>
        > {

    private suspend fun <TPagingRequest : IPagingRequest> firstLoad(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        forceRefresh: Boolean
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val pagingRequest = pagingRequestProvider.initialPagingRequest()
        return pagingSource.load(pagingRequest, forceRefresh)
            .map { result ->
                with(pagingListSegmentConnector) {
                    MultipleSegmentationPagingList(listOf(createNewSegment(result)))
                }
            }
    }

    private suspend fun <TPagingRequest : IPagingRequest> loadInitial(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingRequestProvider, pagingSource, forceRefresh = false)
    }

    private suspend fun <TPagingRequest : IPagingRequest> loadRefresh(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return firstLoad(pagingRequestProvider, pagingSource, forceRefresh = true)
    }

    private suspend fun <TPagingRequest : IPagingRequest> loadPrevious(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: MultipleSegmentationPagingRequestParameter.Previous<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingRequestParameter.pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingRequestProvider, pagingSource)
        }

        val segment = pagingRequestParameter.segments.first()
        if (with(pagingRequestProvider) { segment.canPrevious().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.previousPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    // ToDo: check concat segment or isolated segment
                    MultipleSegmentationPagingList(
                        listOf(segment.createNewSegmentWithPrevious(result)) + pagingRequestParameter.segments.drop(1)
                    )
                }
            }
    }

    private suspend fun <TPagingRequest : IPagingRequest> loadNext(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: MultipleSegmentationPagingRequestParameter.Next<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        if (pagingRequestParameter.pagingList.needInitialLoad(pagingRequestProvider)) {
            return loadInitial(pagingRequestProvider, pagingSource)
        }

        val segment = pagingRequestParameter.segments.last()
        if (with(pagingRequestProvider) { segment.canNext().not() }) {
            return emptyFlow()
        }

        val pagingRequest = with(pagingRequestProvider) { segment.nextPagingRequest() }
        return pagingSource.load(pagingRequest, forceRefresh = false)
            .map { result ->
                with(pagingListSegmentConnector) {
                    // ToDo: check concat segment or isolated segment
                    MultipleSegmentationPagingList(
                        pagingRequestParameter.segments.dropLast(1) + listOf(segment.createNewSegmentWithNext(result))
                    )
                }
            }
    }

    private suspend fun <TPagingRequest : IPagingRequest> loadPreviousBetweenSegments(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: MultipleSegmentationPagingRequestParameter.Between<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val startEdgeSegment = pagingRequestParameter.startEdgeSegment
        val endEdgeSegment = pagingRequestParameter.endEdgeSegment
        if (with(pagingRequestProvider) { endEdgeSegment.canPrevious().not() }) {
            return emptyFlow()
        }

        val startSegments = pagingRequestParameter.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingRequestParameter.segments.takeLastWhile { it != endEdgeSegment }

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

    private suspend fun <TPagingRequest : IPagingRequest> loadNextBetweenSegments(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: MultipleSegmentationPagingRequestParameter.Between<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        val startEdgeSegment = pagingRequestParameter.startEdgeSegment
        val endEdgeSegment = pagingRequestParameter.endEdgeSegment
        if (with(pagingRequestProvider) { startEdgeSegment.canNext().not() }) {
            return emptyFlow()
        }

        val startSegments = pagingRequestParameter.segments.takeWhile { it != startEdgeSegment }
        val endSegments = pagingRequestParameter.segments.takeLastWhile { it != endEdgeSegment }

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

    override suspend fun <TPagingRequest : IPagingRequest> load(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: MultipleSegmentationPagingRequestParameter<TPagingElement, TPagingListSegment>
    ): Flow<MultipleSegmentationPagingList<TPagingElement, TPagingListSegment>> {
        return when (pagingRequestParameter) {
            is MultipleSegmentationPagingRequestParameter.Initial -> loadInitial(pagingRequestProvider, pagingSource)
            is MultipleSegmentationPagingRequestParameter.Refresh -> loadRefresh(pagingRequestProvider, pagingSource)
            is MultipleSegmentationPagingRequestParameter.Previous -> loadPrevious(
                pagingRequestProvider,
                pagingSource,
                pagingRequestParameter
            )
            // ToDo: convert composition function
            is MultipleSegmentationPagingRequestParameter.Between -> loadNextBetweenSegments(
                pagingRequestProvider,
                pagingSource,
                pagingRequestParameter
            )
            is MultipleSegmentationPagingRequestParameter.Next -> loadNext(
                pagingRequestProvider,
                pagingSource,
                pagingRequestParameter
            )
        }
    }
}
