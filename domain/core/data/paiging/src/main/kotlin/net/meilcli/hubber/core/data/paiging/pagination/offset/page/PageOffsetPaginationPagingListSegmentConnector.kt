package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegmentConnector

class PageOffsetPaginationPagingListSegmentConnector<TPagingElement : IPagingElement> :
    IPagingListSegmentConnector<
        TPagingElement,
        PageOffsetPaginationPagingResult<TPagingElement>,
        PageOffsetPaginationPagingListSegment<TPagingElement>
        > {

    override fun createNewSegment(
        result: PageOffsetPaginationPagingResult<TPagingElement>
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        return PageOffsetPaginationPagingListSegment(
            elements = result.elements,
            pages = listOf(result.page),
            reachingStartEdge = result.reachingStartEdge,
            reachingEndEdge = result.reachingEndEdge
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.createNewSegmentWithPrevious(
        result: PageOffsetPaginationPagingResult<TPagingElement>
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(result.page == pages.first() - 1)

        return PageOffsetPaginationPagingListSegment(
            elements = result.elements + elements,
            pages = listOf(result.page) + pages,
            reachingStartEdge = result.reachingStartEdge,
            reachingEndEdge = reachingEndEdge
        )
    }

    override fun PageOffsetPaginationPagingListSegment<TPagingElement>.createNewSegmentWithNext(
        result: PageOffsetPaginationPagingResult<TPagingElement>
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(result.page == pages.last() + 1)

        return PageOffsetPaginationPagingListSegment(
            elements = elements + result.elements,
            pages = pages + listOf(result.page),
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = result.reachingEndEdge
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
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextSegment.reachingEndEdge
        )
    }
}
