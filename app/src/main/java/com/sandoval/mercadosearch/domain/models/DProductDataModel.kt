package com.sandoval.mercadosearch.domain.models

data class DProductDataModel(
    val id: String,
    val title: String = "",
    val price: Double = 0.0,
    val condition: ProductConditionEntity,
    val permalink: String = "",
    val thumbnail: String = "",
    val address: ProductAddressEntity,
    val freeShipping: Boolean = false
) {

    enum class ProductConditionEntity {
        NEW, USED, UNKNOWN
    }

    data class ProductAddressEntity(val city: String = "", val state: String = "")

}