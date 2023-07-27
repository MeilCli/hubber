package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingResult

data class PageOffsetPaginationPagingResult<TPagingElement : IPagingElement>(
    val page: Int,
    override val elements: List<TPagingElement>,
    override val reachingStartEdge: Boolean,
    override val reachingEndEdge: Boolean
) : IPagingResult<TPagingElement>
