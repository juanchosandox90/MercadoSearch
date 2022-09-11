package com.sandoval.mercadosearch.domain.repository

import com.sandoval.mercadosearch.domain.base.PaginatedDProductDataModel
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.models.products.DProductDataModel
import com.sandoval.mercadosearch.domain.models.SearchProductsParams
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel

interface ProductsRepository {
    suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDProductDataModel<DProductDataModel>>
    suspend fun getDetails(id: String): Result<DProductDetailsDataModel>
}