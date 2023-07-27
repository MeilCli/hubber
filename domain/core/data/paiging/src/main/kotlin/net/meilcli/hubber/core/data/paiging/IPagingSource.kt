package net.meilcli.hubber.core.data.paiging

import kotlinx.coroutines.flow.Flow

interface IPagingSource<TPagingElement : IPagingElement, TPagingRequest : IPagingRequest, TPagingResult : IPagingResult<TPagingElement>> {

    suspend fun load(pagingRequest: TPagingRequest, forceRefresh: Boolean): Flow<TPagingResult>
}
