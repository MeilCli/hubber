package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegmentConnector

class PageOffsetPaginationPagingListSegmentConnector<TPagingElement : IPagingElement> :
    IPagingListSegmentConnector<TPagingElement, PageOffsetPaginationPagingRequest, PageOffsetPaginationPagingListSegment<TPagingElement>> {

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.createNewSegmentWithInitial(
        initialElements: List<TPagingElement>,
        reachingStartEdge: Boolean,
        reachingEndEdge: Boolean
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        return PageOffsetPaginationPagingListSegment(
            elements = initialElements,
            pages = pages,
            countPerRequest = countPerRequest,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = reachingEndEdge
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.createNewSegmentWithPrevious(
        previousElements: List<TPagingElement>,
        pagingRequest: PageOffsetPaginationPagingRequest
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(pagingRequest.page == pages.first() - 1)
        check(pagingRequest.countPerRequest == countPerRequest)

        return PageOffsetPaginationPagingListSegment(
            elements = previousElements + elements,
            pages = listOf(pagingRequest.page) + pages,
            countPerRequest = countPerRequest,
            reachingStartEdge = pagingRequest.page <= 1,
            reachingEndEdge = reachingEndEdge
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.createNewSegmentWithNext(
        nextElements: List<TPagingElement>,
        pagingRequest: PageOffsetPaginationPagingRequest
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(pagingRequest.page == pages.last() + 1)
        check(pagingRequest.countPerRequest == countPerRequest)

        return PageOffsetPaginationPagingListSegment(
            elements = elements + nextElements,
            pages = pages + listOf(pagingRequest.page),
            countPerRequest = countPerRequest,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextElements.size < countPerRequest
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.needsConcat(
        nextSegment: PageOffsetPaginationPagingListSegment<TPagingElement>
    ): Boolean {
        return pages.intersect(nextSegment.pages.toSet()).isNotEmpty()
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.concatWithNextSegment(
        nextSegment: PageOffsetPaginationPagingListSegment<TPagingElement>
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(needsConcat(nextSegment))

        val newElements = elements + nextSegment.elements.dropWhile { e -> elements.any { it.isSameTo(e) } }
        val newPages = (pages + nextSegment.pages).distinct().sorted()

        return PageOffsetPaginationPagingListSegment(
            elements = newElements,
            pages = newPages,
            countPerRequest = countPerRequest,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextSegment.reachingEndEdge
        )
    }
}
