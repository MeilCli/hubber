package net.meilcli.hubber.core.data.paiging

import kotlinx.coroutines.flow.Flow

interface IPager<
    TPagingElement : IPagingElement,
    TPagingRequestParameter : IPagingRequestParameter,
    TPagingResult : IPagingResult<TPagingElement>,
    TPagingListSegment : IPagingListSegment<TPagingElement>,
    TPagingList : IPagingList<TPagingElement, TPagingListSegment>
    > {

    suspend fun <TPagingRequest : IPagingRequest> load(
        pagingRequestProvider: IPagingRequestProvider<TPagingElement, TPagingRequest, TPagingRequestParameter, TPagingListSegment>,
        pagingSource: IPagingSource<TPagingElement, TPagingRequest, TPagingResult>,
        pagingRequestParameter: TPagingRequestParameter
    ): Flow<TPagingList>
}
