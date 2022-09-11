package com.sandoval.mercadosearch.data.datasource.remote.models.products

import com.sandoval.mercadosearch.domain.models.products.DProductDataModel

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