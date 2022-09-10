package com.sandoval.mercadosearch.data.datasource.remote.models

data class SearchProductsParams(
    val name: String, val offset: Int, val limit: Int
)
