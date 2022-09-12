package com.sandoval.mercadosearch.ui.viewmodel.models.details_product

import androidx.compose.ui.graphics.Color

data class ProductDetailDataUIModel(
    val availableQuantity: String,
    val availableQuantityColor: Color = Color.LightGray,
    val soldQuantity: String,
    val warranty: String,
    val pictures: List<ProductPictureUIModel>,
    val attributes: List<ProductAttributeUIModel>
)