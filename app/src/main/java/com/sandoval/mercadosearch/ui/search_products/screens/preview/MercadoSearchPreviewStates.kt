package com.sandoval.mercadosearch.ui.search_products.screens.preview

import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.search_products.screens.SearchResultsActions
import com.sandoval.mercadosearch.ui.viewmodel.mapper.DProductDataModelToUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState


val error =
    ErrorUIModel(generalPurposeMessage = "Something went wrong", suggestedAction = "Retry")

val product = ProductDataUIModel(
    id = "s",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val product2 = ProductDataUIModel(
    id = "si",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val product3 = ProductDataUIModel(
    id = "sa",
    name = "Product",
    price = "$20",
    picture = "",
    freeShipping = true,
    condition = "New",
    address = "Miami, Florida",
    shareableDescription = "Description",
    storeLink = ""
)

val productList = listOf(product, product2, product3)

val searchResultActions = SearchResultsActions(
    doWhenSearchActionClicked = {},
    doWhenSearchedTextChanged = {},
    doWhenBackButtonClicked = {})

val searchResultsLoading = ProductSearchState.Loading

val searchResults = ProductSearchState.Results(products = productList)

val searchResultsNoResults = ProductSearchState.NoResults

val searchResultsRefreshingError =
    ProductSearchState.Results(products = productList, refreshingError = error)

val searchResultsFailure = ProductSearchState.Failure(
    ErrorUIModel(
        generalPurposeMessage = "Opps! Something went wrong &#128550;",
        suggestedAction = "Retry"
    )
)
