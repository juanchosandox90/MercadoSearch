package com.sandoval.mercadosearch.ui.viewmodel

import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductDetailDataUIModel

val productModel = ProductDataUIModel(
    id = "ID20",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val productDetailsModel = ProductDetailDataUIModel(
    availableQuantity = "1",
    soldQuantity = "1",
    warranty = "No warranty",
    pictures = emptyList(),
    attributes = emptyList()
)

val errorUIModel =
    ErrorUIModel(generalPurposeMessage = "Something went wrong", suggestedAction = "Retry")