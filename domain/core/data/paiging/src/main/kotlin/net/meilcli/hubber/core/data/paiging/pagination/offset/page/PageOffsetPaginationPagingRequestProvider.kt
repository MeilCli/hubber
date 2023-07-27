package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingRequestProvider

class PageOffsetPaginationPagingRequestProvider<TPagingElement : IPagingElement> :
    IPagingRequestProvider<TPagingElement, PageOffsetPaginationPagingRequest, PageOffsetPaginationPagingListSegment<TPagingElement>> {

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.needInitialLoad(): Boolean {
        return elements.isEmpty()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.initialPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.first(),
            countPerRequest = countPerRequest
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.canPrevious(): Boolean {
        return reachingStartEdge.not()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.previousPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.first() - 1,
            countPerRequest = countPerRequest
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.canNext(): Boolean {
        return reachingEndEdge.not()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.nextPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.last() + 1,
            countPerRequest = countPerRequest
        )
    }
}
