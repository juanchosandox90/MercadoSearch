package com.sandoval.mercadosearch.data.datasource.remote.mapper

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.sandoval.mercadosearch.data.datasource.remote.models.PaginatedDataEntity
import com.sandoval.mercadosearch.data.datasource.remote.models.ProductDataModel
import com.sandoval.mercadosearch.data.extensions.getAsJsonObjectOrNull
import com.sandoval.mercadosearch.data.extensions.getOrNull
import com.sandoval.mercadosearch.data.json.JsonResponseMapper
import com.sandoval.mercadosearch.data.networking.ErrorEntity
import com.sandoval.mercadosearch.data.networking.Result
import com.sandoval.mercadosearch.data.pagination.PaginationUtils


object JsonToPaginatedProductsData :
    JsonResponseMapper<JsonObject, PaginatedDataEntity<ProductDataModel>>() {

    override fun map(
        responseCode: Int,
        json: JsonObject
    ): Result<PaginatedDataEntity<ProductDataModel>> {
        val paging = json.getAsJsonObjectOrNull(PAGING_OBJECT_KEY)
        val total = paging?.getOrNull(TOTAL_OBJECT_KEY)?.asInt?.run {

            /**
             * API requires an access token for more than 1000 results :eyes:
             * */

            if (this > MAX_UNAUTHENTICATED_LIMIT) MAX_UNAUTHENTICATED_LIMIT else this
        }
        val limit = paging?.getOrNull(LIMIT_OBJECT_KEY)?.asInt
        return if (total != null && limit != null) {
            val pages = PaginationUtils.calculatePages(total, limit)
            Result.Success(
                PaginatedDataEntity(
                    total = total,
                    pages = pages,
                    items = mapJsonArray(json.getAsJsonArray(RESULTS_OBJECT_KEY))
                )
            )
        } else {
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = responseCode,
                    message = "No information provided for pagination"
                )
            )
        }
    }

    private fun mapJsonArray(jsonArray: JsonArray): List<ProductDataModel> {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
            .fromJson<List<ProductDataModel>?>(
                jsonArray,
                object : TypeToken<List<ProductDataModel>>() {}.type
            ).map { productModel ->
                mapModelToEntity(productModel)
            }
    }

    private fun mapModelToEntity(productModel: ProductDataModel): ProductDataModel =
        ProductDataModel(
            id = productModel.id,
            title = productModel.title ?: "",
            price = productModel.price ?: 0.0,
            permalink = productModel.permalink ?: "",
            thumbnail = productModel.thumbnail ?: "",
            shipping = productModel.shipping,
            address = ProductDataModel.AddressModel(
                cityName = productModel.address?.cityName ?: "",
                stateName = productModel.address?.stateName ?: ""
            )
        )

    private const val PAGING_OBJECT_KEY = "paging"
    private const val TOTAL_OBJECT_KEY = "total"
    private const val LIMIT_OBJECT_KEY = "limit"
    private const val RESULTS_OBJECT_KEY = "results"

    private const val MAX_UNAUTHENTICATED_LIMIT = 1000

}