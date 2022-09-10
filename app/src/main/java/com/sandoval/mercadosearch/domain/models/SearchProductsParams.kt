package com.sandoval.mercadosearch.domain.models

data class SearchProductsParams(
    val name: String, val offset: Int, val limit: Int
)