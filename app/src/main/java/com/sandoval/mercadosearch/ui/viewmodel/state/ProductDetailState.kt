package com.sandoval.mercadosearch.ui.viewmodel.state

import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductDetailDataUIModel

sealed class ProductDetailState (val product: ProductDataUIModel) {

    class Loading(product: ProductDataUIModel) : ProductDetailState(product) {
        override fun equals(other: Any?): Boolean = other is Loading
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class DetailsReady(product: ProductDataUIModel, val details: ProductDetailDataUIModel) :
        ProductDetailState(product) {
        override fun equals(other: Any?): Boolean = other is DetailsReady
        override fun hashCode(): Int = javaClass.hashCode()
    }

    class Failure(product: ProductDataUIModel, val error: ErrorUIModel) : ProductDetailState(product) {
        override fun equals(other: Any?): Boolean = other is Failure
        override fun hashCode(): Int = javaClass.hashCode()
    }
}