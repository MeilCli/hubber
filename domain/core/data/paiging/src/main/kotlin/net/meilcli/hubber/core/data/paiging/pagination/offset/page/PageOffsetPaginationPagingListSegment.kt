package net.meilcli.hubber.core.data.paiging.pagination.offset.page

import net.meilcli.hubber.core.data.paiging.IPagingElement
import net.meilcli.hubber.core.data.paiging.IPagingListSegment

class PageOffsetPaginationPagingListSegment<TPagingElement : IPagingElement> internal constructor(
    val elements: List<TPagingElement>,
    val pages: List<Int>,
    override val countPerRequest: Int,
    override val reachingStartEdge: Boolean,
    override val reachingEndEdge: Boolean
) : IPagingListSegment<TPagingElement>,
    List<TPagingElement> by elements {

    companion object {

        fun <TPagingElement : IPagingElement> factory(): Factory<TPagingElement> {
            return Factory()
        }
    }

    class Factory<TPagingElement : IPagingElement> :
        IPagingListSegment.IPagingListSegmentFactory<TPagingElement, PageOffsetPaginationPagingListSegment<TPagingElement>> {

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
}
