package com.sandoval.mercadosearch.domain.models.details_product

data class DProductDetailsDataModel(
    val availableQuantity: Int = 0,
    val soldQuantity: Int = 0,
    val attributes: List<DProductAttributes> = emptyList(),
    val pictures: List<String> = emptyList(),
    val warranty: String = ""
)
