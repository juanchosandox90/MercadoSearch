package com.sandoval.mercadosearch.domain.base

data class PaginatedDProductDataModel<T>(
    val total: Int,
    val pages: Int,
    val items: List<T>
) {
    fun isNotEmpty(): Boolean = items.isNotEmpty()
    fun isEmpty(): Boolean = items.isEmpty()
}