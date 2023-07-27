package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingRequest

data class PageOffsetPaginationPagingRequest(
    val page: Int,
    val countPerRequest: Int
) : IPagingRequest
