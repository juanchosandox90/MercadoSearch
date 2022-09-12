package com.sandoval.mercadosearch.data.datasource.remote.mapper

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.sandoval.mercadosearch.domain.base.PaginatedDProductDataModel
import com.sandoval.mercadosearch.data.datasource.remote.models.products.ProductDataModel
import com.sandoval.mercadosearch.data.extensions.getAsJsonObjectOrNull
import com.sandoval.mercadosearch.data.extensions.getOrNull
import com.sandoval.mercadosearch.data.json.JsonResponseMapper
import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.data.pagination.PaginationUtils
import com.sandoval.mercadosearch.domain.models.products.DProductDataModel

object JsonToPaginatedProductsData :
    JsonResponseMapper<JsonObject, PaginatedDProductDataModel<DProductDataModel>>() {

    override fun map(
        responseCode: Int,
        json: JsonObject
    ): Result<PaginatedDProductDataModel<DProductDataModel>> {
        val paging = json.getAsJsonObjectOrNull(PAGING_OBJECT_KEY)
        val total = paging?.getOrNull(TOTAL_OBJECT_KEY)?.asInt?.run {

            /**
             * API requiere un token de acceso para mas de 1000 resultados
             * */

            if (this > MAX_UNAUTHENTICATED_LIMIT) MAX_UNAUTHENTICATED_LIMIT else this
        }
        val limit = paging?.getOrNull(LIMIT_OBJECT_KEY)?.asInt
        return if (total != null && limit != null) {
            val pages = PaginationUtils.calculatePages(total, limit)
            Result.Success(
                PaginatedDProductDataModel(
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

    private fun mapJsonArray(jsonArray: JsonArray): List<DProductDataModel> {
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

    private fun mapModelToEntity(productModel: ProductDataModel): DProductDataModel =
        DProductDataModel(
            id = productModel.id,
            title = productModel.title ?: "",
            price = productModel.price ?: 0.0,
            condition = productModel.condition
                ?: DProductDataModel.ProductConditionEntity.UNKNOWN,
            permalink = productModel.permalink ?: "",
            thumbnail = productModel.thumbnail ?: "",
            freeShipping = productModel.shipping?.freeShipping ?: false,
            address = DProductDataModel.ProductAddressEntity(
                city = productModel.address?.cityName ?: "",
                state = productModel.address?.stateName ?: ""
            )
        )

    private const val PAGING_OBJECT_KEY = "paging"
    private const val TOTAL_OBJECT_KEY = "total"
    private const val LIMIT_OBJECT_KEY = "limit"
    private const val RESULTS_OBJECT_KEY = "results"

    private const val MAX_UNAUTHENTICATED_LIMIT = 1000

}