package com.sandoval.mercadosearch.ui.viewmodel.models.products

data class ProductDataUIModel(
    val id: String,
    val name: String,
    val price: String,
    val picture: String,
    val freeShipping: Boolean,
    val condition: String,
    val address: String,
    val shareableDescription: String,
    val storeLink: String
)
