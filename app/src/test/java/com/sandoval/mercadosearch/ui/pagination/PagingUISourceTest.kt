package com.sandoval.mercadosearch.ui.pagination

import org.junit.Assert
import org.junit.Test

class PagingUISourceTest {

    @Test
    fun canFetchWithOutInit() {
        val pagingUISource = PagingUISource<String>(limit = 20)
        Assert.assertFalse(pagingUISource.canFetch())
    }

    @Test
    fun dataLongerThanTotal() {
        val pagingUISource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertFalse(pagingUISource.append(List(15) {}))
    }

    @Test
    fun dataEqualThanTotal() {
        val pagingUISource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertTrue(pagingUISource.append(List(10) {}))
    }

    @Test
    fun dataLowerThanTotal() {
        val pagingUISource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
        }
        Assert.assertTrue(pagingUISource.append(List(5) {}))
    }

    @Test
    fun canFetchDataFilled() {
        val pagingUISource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
            append(List(10) {})
        }
        Assert.assertFalse(pagingUISource.canFetch())
    }

    @Test
    fun canFetch() {
        val pagingUISource = PagingUISource<Unit>(limit = 5).apply {
            init(pages = 2, total = 10)
            append(List(5) {})
        }
        Assert.assertTrue(pagingUISource.canFetch())
    }
}