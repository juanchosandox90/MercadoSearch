package com.sandoval.mercadosearch.data.datasource.remote

import com.sandoval.mercadosearch.domain.base.PaginatedDProductDataModel
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.models.DProductDataModel
import com.sandoval.mercadosearch.domain.models.SearchProductsParams

interface RemoteProductsDataSource {
    suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDProductDataModel<DProductDataModel>>
}