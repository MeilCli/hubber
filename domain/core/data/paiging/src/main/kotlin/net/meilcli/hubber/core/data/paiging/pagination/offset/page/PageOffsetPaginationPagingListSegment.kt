package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment

@Suppress("detekt.TooManyFunctions")
class PageOffsetPaginationPagingListSegment<TPagingElement : IPagingElement> internal constructor(
    val elements: List<TPagingElement>,
    val pages: List<Int>,
    override val countPerRequest: Int,
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

        override fun createEmpty(initialPage: Int, countPerRequest: Int): PageOffsetPaginationPagingListSegment<TPagingElement> {
            return PageOffsetPaginationPagingListSegment(
                elements = emptyList(),
                pages = listOf(initialPage),
                countPerRequest = countPerRequest,
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
            countPerRequest = countPerRequest
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
            countPerRequest = countPerRequest,
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
            countPerRequest = countPerRequest
        )
    }

    override fun createNewSegmentWithPrevious(
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

    override fun canNext(): Boolean {
        return reachingEndEdge.not()
    }

    override fun nextPagingRequest(): PageOffsetPaginationPagingRequest {
        return PageOffsetPaginationPagingRequest(
            page = pages.last() + 1,
            countPerRequest = countPerRequest
        )
    }

    override fun createNewSegmentWithNext(
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
            countPerRequest = countPerRequest,
            reachingStartEdge = reachingStartEdge,
            reachingEndEdge = nextSegment.reachingEndEdge
        )
    }
}
