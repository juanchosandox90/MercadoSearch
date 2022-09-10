package com.sandoval.mercadosearch.data.datasource.remote.models

import com.sandoval.mercadosearch.domain.models.DProductDataModel

class ProductDataModel(
    val id: String,
    val title: String?,
    val price: Double?,
    val condition: DProductDataModel.ProductConditionEntity?,
    val permalink: String?,
    val thumbnail: String?,
    val address: AddressModel?,
    val shipping: ProductShippingModel?
) {
    data class AddressModel(
        val stateName: String?,
        val cityName: String?
    )

    data class ProductShippingModel(val freeShipping: Boolean?)

}