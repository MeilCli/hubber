package net.meilcli.hubber.core.data.paiging

import kotlinx.coroutines.flow.Flow

interface IPager<
    TPagingElement : IPagingElement,
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>,
    TPagingList : IPagingList<TPagingElement, TPagingListSegment>
    > {

    suspend fun <TPagingRequest : IPagingRequest> loadInitial(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<TPagingList>

    suspend fun <TPagingRequest : IPagingRequest> loadRefresh(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>
    ): Flow<TPagingList>

    suspend fun <TPagingRequest : IPagingRequest> loadPrevious(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: TPagingList
    ): Flow<TPagingList>

    suspend fun <TPagingRequest : IPagingRequest> loadNext(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingList: TPagingList
    ): Flow<TPagingList>
}
