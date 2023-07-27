package net.meilcli.hubber.core.data.paiging

import kotlinx.coroutines.flow.Flow

interface IPager<
    TPagingElement : IPagingElement,
    TPagingRequest : IPagingRequest,
    TPagingListSegment : IPagingListSegment<TPagingElement>,
    TPagingList : IPagingList<TPagingElement, TPagingListSegment>
    > {

    suspend fun loadInitial(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: TPagingList
    ): Flow<TPagingList>

    suspend fun loadRefresh(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: TPagingList
    ): Flow<TPagingList>

    suspend fun loadPrevious(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: TPagingList
    ): Flow<TPagingList>

    suspend fun loadNext(
        pagingSource: IPagingSource<TPagingElement, TPagingRequest>,
        pagingList: TPagingList
    ): Flow<TPagingList>
}
