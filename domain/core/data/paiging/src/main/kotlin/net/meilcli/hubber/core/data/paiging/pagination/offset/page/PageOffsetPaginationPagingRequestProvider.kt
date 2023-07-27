package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingRequestProvider

class PageOffsetPaginationPagingRequestProvider<TPagingElement : IPagingElement, TPagingRequest : PageOffsetPaginationPagingRequest>(
    private val initialPage: Int,
    private val countPerRequest: Int,
    private val pagingRequestCreator: IPagingRequestCreator<TPagingRequest>
) : IPagingRequestProvider<TPagingElement, TPagingRequest, PageOffsetPaginationPagingListSegment<TPagingElement>> {

    fun interface IPagingRequestCreator<TPagingRequest : PageOffsetPaginationPagingRequest> {

        fun create(page: Int, countPerRequest: Int): TPagingRequest
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.needInitialLoad(): Boolean {
        return elements.isEmpty()
    }

    override fun initialPagingRequest(): TPagingRequest {
        return pagingRequestCreator.create(page = initialPage, countPerRequest = countPerRequest)
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.canPrevious(): Boolean {
        return reachingStartEdge.not()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.previousPagingRequest(): TPagingRequest {
        return pagingRequestCreator.create(
            page = pages.first() - 1,
            countPerRequest = this@PageOffsetPaginationPagingRequestProvider.countPerRequest
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.canNext(): Boolean {
        return reachingEndEdge.not()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.nextPagingRequest(): TPagingRequest {
        return pagingRequestCreator.create(
            page = pages.last() + 1,
            countPerRequest = this@PageOffsetPaginationPagingRequestProvider.countPerRequest
        )
    }
}
