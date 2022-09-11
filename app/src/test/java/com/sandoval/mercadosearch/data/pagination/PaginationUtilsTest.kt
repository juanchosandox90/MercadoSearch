package com.sandoval.mercadosearch.data.pagination

import junit.framework.Assert.assertEquals
import org.junit.Test

class PaginationUtilsTest {

    @Test
    fun divisionByZero() {
        val actual = PaginationUtils.calculatePages(total = 1000, limit = 0)
        val expected = 0
        assertEquals(expected, actual)
    }

    @Test
    fun lowerThanNextInt() {
        val actual = PaginationUtils.calculatePages(total = 1111, limit = 20)
        val expected = 56
        assertEquals(expected, actual)
    }

    @Test
    fun higherThanNextInt() {
        val actual = PaginationUtils.calculatePages(total = 1125, limit = 20)
        val expected = 57
        assertEquals(expected, actual)
    }
}