package com.sandoval.mercadosearch.data.datasource.remote

import com.sandoval.mercadosearch.data.datasource.remote.models.PaginatedDataEntity
import com.sandoval.mercadosearch.data.datasource.remote.models.ProductDataModel
import com.sandoval.mercadosearch.data.datasource.remote.models.SearchProductsParams
import com.sandoval.mercadosearch.data.networking.Result

interface RemoteProductsDataSource {
    /*
    TODO  este metodo se trae toda la informacion paginada. Actualmente lo hara la data pero en un futuro, Se hara en la capa de dominio
     */
    suspend fun searchByName(params: SearchProductsParams): Result<PaginatedDataEntity<ProductDataModel>>
}