package com.sandoval.mercadosearch.data.datasource.remote.models.details_product

data class ProductDetailsDataModel(
    val availableQuantity: Int?,
    val soldQuantity: Int?,
    val warranty: String?,
    val pictures: List<PictureModel>?,
    val attributes: List<AttributesModel>?
)