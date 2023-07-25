package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment

@Suppress("detekt.TooManyFunctions")
class PageOffsetPaginationPagingListSegment<TPagingElement : IPagingElement> internal constructor(
    val elements: List<TPagingElement>,
    val pages: List<Int>,
    override val countPerPage: Int,
    override val reachingStartEdge: Boolean,
    override val reachingEndEdge: Boolean
) : IPagingListSegment<TPagingElement, PageOffsetPaginationPagingRequest, PageOffsetPaginationPagingListSegment<TPagingElement>>,
    List<TPagingElement> by elements {

    companion object {

        fun <TPagingElement : IPagingElement> factory(): Factory<TPagingElement> {
            return Factory()
        }
    }

    class Factory<TPagingElement : IPagingElement> :
        IPagingListSegment.IPagingListSegmentFactory<
            TPagingElement,
            PageOffsetPaginationPagingRequest,
            PageOffsetPaginationPagingListSegment<TPagingElement>
            > {

        override fun createEmpty(initialPage: Int, countPerPage: Int): PageOffsetPaginationPagingListSegment<TPagingElement> {
            return PageOffsetPaginationPagingListSegment(
                elements = emptyList(),
                pages = listOf(initialPage),
                countPerPage = countPerPage,
                reachingStartEdge = false,
                reachingEndEdge = false
            )
        }
    }

    override fun needInitialLoad(): Boolean {
        return elements.isEmpty()
    }

    override fun initialPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.first(),
            countPerPage = countPerPage
        )
    }

    override fun createNewSegmentWithInitial(
        initialElements: List<TPagingElement>,
        reachingStartEdge: Boolean,
        reachingEndEdge: Boolean
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        return PageOffsetPaginationPagingListSegment(
            elements = initialElements,
            pages = pages,
            countPerPage = countPerPage,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = reachingEndEdge
        )
    }

    override fun canPrevious(): Boolean {
        return reachingStartEdge.not()
    }

    override fun previousPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.first() - 1,
            countPerPage = countPerPage
        )
    }

    override fun createNewSegmentWithPrevious(
        previousElements: List<TPagingElement>,
        pagingRequest: PageOffsetPaginationPagingRequest
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(pagingRequest.page == pages.first() - 1)
        check(pagingRequest.countPerPage == countPerPage)

        return PageOffsetPaginationPagingListSegment(
            elements = previousElements + elements,
            pages = listOf(pagingRequest.page) + pages,
            countPerPage = countPerPage,
            reachingStartEdge = pagingRequest.page <= 1,
            reachingEndEdge = reachingEndEdge
        )
    }

    override fun canNext(): Boolean {
        return reachingEndEdge.not()
    }

    override fun nextPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.last() + 1,
            countPerPage = countPerPage
        )
    }

    override fun createNewSegmentWithNext(
        nextElements: List<TPagingElement>,
        pagingRequest: PageOffsetPaginationPagingRequest
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(pagingRequest.page == pages.last() + 1)
        check(pagingRequest.countPerPage == countPerPage)

        return PageOffsetPaginationPagingListSegment(
            elements = elements + nextElements,
            pages = pages + listOf(pagingRequest.page),
            countPerPage = countPerPage,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextElements.size < countPerPage
        )
    }

    override fun needsConcat(nextSegment: PageOffsetPaginationPagingListSegment<TPagingElement>): Boolean {
        return pages.intersect(nextSegment.pages.toSet()).isNotEmpty()
    }

    override fun concatWithNextSegment(
        nextSegment: PageOffsetPaginationPagingListSegment<TPagingElement>
    ): PageOffsetPaginationPagingListSegment<TPagingElement> {
        check(needsConcat(nextSegment))

        val newElements = elements + nextSegment.elements.dropWhile { e -> elements.any { it.isSameTo(e) } }
        val newPages = (pages + nextSegment.pages).distinct().sorted()

        return PageOffsetPaginationPagingListSegment(
            elements = newElements,
            pages = newPages,
            countPerPage = countPerPage,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextSegment.reachingEndEdge
        )
    }
}
