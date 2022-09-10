package com.sandoval.mercadosearch.data.datasource.remote.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sandoval.mercadosearch.common.GET_ITEM_DETAIL
import com.sandoval.mercadosearch.common.SEARCH_BY_NAME_PATH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ProductsApiService {

    @GET(SEARCH_BY_NAME_PATH)
    suspend fun searchByName(
        @Path("sideId") sideId: String,
        @QueryMap query: HashMap<String, Any>
    ): Response<JsonObject>

    @GET(GET_ITEM_DETAIL)
    suspend fun getDetails(@Query("ids") id: String): Response<JsonArray>
}