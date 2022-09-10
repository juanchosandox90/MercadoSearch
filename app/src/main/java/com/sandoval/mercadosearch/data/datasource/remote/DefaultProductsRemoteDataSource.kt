package com.sandoval.mercadosearch.data.datasource.remote

import com.sandoval.mercadosearch.common.DEFAULT_SIDE_ID
import com.sandoval.mercadosearch.common.LIMIT_PARAM_KEY
import com.sandoval.mercadosearch.common.OFFSET_PARAM_KEY
import com.sandoval.mercadosearch.common.QUERY_PARAM_KEY
import com.sandoval.mercadosearch.data.datasource.remote.api.ProductsApiService
import com.sandoval.mercadosearch.data.datasource.remote.mapper.JsonToPaginatedProductsData
import com.sandoval.mercadosearch.data.datasource.remote.models.PaginatedDataEntity
import com.sandoval.mercadosearch.data.datasource.remote.models.ProductDataModel
import com.sandoval.mercadosearch.data.datasource.remote.models.SearchProductsParams
import com.sandoval.mercadosearch.data.networking.NetworkConnectionChecker
import com.sandoval.mercadosearch.data.networking.Result
import com.sandoval.mercadosearch.data.networking.networkRequest
import javax.inject.Inject

class DefaultProductsRemoteDataSource @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val networkConnectionChecker: NetworkConnectionChecker
) : RemoteProductsDataSource {

    override suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDataEntity<ProductDataModel>> =
        networkRequest(networkConnectionChecker) {
            JsonToPaginatedProductsData.map(
                productsApiService.searchByName(
                    sideId = DEFAULT_SIDE_ID,
                    query = hashMapOf(
                        QUERY_PARAM_KEY to params.name,
                        OFFSET_PARAM_KEY to params.offset,
                        LIMIT_PARAM_KEY to params.limit
                    )
                )
            )
        }
}
