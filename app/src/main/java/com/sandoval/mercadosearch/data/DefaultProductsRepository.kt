package com.sandoval.mercadosearch.data

import com.sandoval.mercadosearch.data.datasource.remote.DefaultProductsRemoteDataSource
import com.sandoval.mercadosearch.data.datasource.remote.models.PaginatedDataEntity
import com.sandoval.mercadosearch.data.datasource.remote.models.ProductDataModel
import com.sandoval.mercadosearch.data.datasource.remote.models.SearchProductsParams
import com.sandoval.mercadosearch.data.networking.Result
import javax.inject.Inject

class DefaultProductsRepository @Inject constructor(private val remoteDataSource: DefaultProductsRemoteDataSource) :
    ProductsRepository {
    override suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDataEntity<ProductDataModel>> =
        remoteDataSource.searchByName(params)
}