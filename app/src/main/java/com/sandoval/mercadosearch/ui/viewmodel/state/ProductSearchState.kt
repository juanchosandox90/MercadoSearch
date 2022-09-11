package com.sandoval.mercadosearch.ui.viewmodel.state

import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel

sealed class ProductSearchState {

    object Loading : ProductSearchState()
    object EmptySearchedText : ProductSearchState()
    object NoResults : ProductSearchState()
    class Results(
        val products: List<ProductDataUIModel>,
        val isRefreshing: Boolean = false,
        val refreshingError: ErrorUIModel? = null
    ) : ProductSearchState()

    class Failure(val error: ErrorUIModel) : ProductSearchState() {
        override fun equals(other: Any?): Boolean = other is Failure
                && other.error.getWarningMessage() == error.getWarningMessage()

        override fun hashCode(): Int = error.hashCode()
    }
}