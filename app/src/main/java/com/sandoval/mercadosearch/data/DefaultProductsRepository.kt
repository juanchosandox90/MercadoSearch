package com.sandoval.mercadosearch.data

import com.sandoval.mercadosearch.data.datasource.remote.DefaultProductsRemoteDataSource
import com.sandoval.mercadosearch.domain.base.PaginatedDProductDataModel
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.models.products.DProductDataModel
import com.sandoval.mercadosearch.domain.repository.ProductsRepository
import com.sandoval.mercadosearch.domain.models.SearchProductsParams
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel
import javax.inject.Inject

class DefaultProductsRepository @Inject constructor(private val remoteDataSource: DefaultProductsRemoteDataSource) :
    ProductsRepository {
    override suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDProductDataModel<DProductDataModel>> =
        remoteDataSource.searchByName(params)

    override suspend fun getDetails(id: String): Result<DProductDetailsDataModel> =
        remoteDataSource.getDetails(id)
}