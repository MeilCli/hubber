package net.meilcli.hubber.core.data.paiging

import kotlinx.coroutines.flow.Flow

interface IPagingSource<TPagingElement : IPagingElement, TPagingRequest : IPagingRequest> {

    data class FirstLoadResult<TPagingElement : IPagingElement>(
        val elements: List<TPagingElement>,
        val reachingStartEdge: Boolean,
        val reachingEndEdge: Boolean
    )

    suspend fun firstLoad(pagingRequest: TPagingRequest, forceRefresh: Boolean): Flow<FirstLoadResult<TPagingElement>>

    suspend fun load(pagingRequest: TPagingRequest): Flow<List<TPagingElement>>
}
