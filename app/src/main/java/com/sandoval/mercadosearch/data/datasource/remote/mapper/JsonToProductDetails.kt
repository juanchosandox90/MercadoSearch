package com.sandoval.mercadosearch.data.datasource.remote.mapper

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.sandoval.mercadosearch.common.BODY_OBJECT_KEY
import com.sandoval.mercadosearch.common.CODE_OBJECT_KEY
import com.sandoval.mercadosearch.data.datasource.remote.models.details_product.ProductDetailsDataModel
import com.sandoval.mercadosearch.data.extensions.getAsJsonObjectOrNull
import com.sandoval.mercadosearch.data.extensions.getOrNull
import com.sandoval.mercadosearch.data.json.JsonResponseMapper
import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.models.details_product.DProductAttributes
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel

object JsonToProductDetails : JsonResponseMapper<JsonArray, DProductDetailsDataModel>() {

    override fun map(responseCode: Int, json: JsonArray): Result<DProductDetailsDataModel> {
        val item = json.firstOrNull()?.getAsJsonObjectOrNull()
        val body = item?.getAsJsonObjectOrNull(BODY_OBJECT_KEY)
        val requestCode = item?.getOrNull(CODE_OBJECT_KEY)?.asInt
        return if (body != null) {
            Result.Success(mapJsonObject(body).run {
                DProductDetailsDataModel(availableQuantity = this.availableQuantity ?: 0,
                    soldQuantity = this.soldQuantity ?: 0,
                    warranty = this.warranty ?: "",
                    pictures = this.pictures?.mapNotNull { picture -> picture.secureUrl }
                        ?: emptyList(),
                    attributes = this.attributes?.mapNotNull { attribute ->
                        if (!attribute.name.isNullOrBlank() && !attribute.valueName.isNullOrBlank()) {
                            DProductAttributes(
                                name = attribute.name,
                                value = attribute.valueName
                            )
                        } else {
                            null
                        }
                    } ?: emptyList()
                )
            })
        } else {
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = requestCode ?: responseCode,
                    message = "Invalid response"
                )
            )
        }
    }

    private fun mapJsonObject(jsonObject: JsonObject): ProductDetailsDataModel {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
            .fromJson(
                jsonObject,
                object : TypeToken<ProductDetailsDataModel>() {}.type
            )
    }

}