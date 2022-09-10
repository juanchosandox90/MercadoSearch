package com.sandoval.mercadosearch.data.datasource.remote.models

class ProductDataModel(
    val id: String,
    val title: String?,
    val price: Double?,
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

//TODO Pending to map condition property