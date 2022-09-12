package com.sandoval.mercadosearch.ui.search_products.screens.preview

import androidx.compose.ui.graphics.Color
import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.search_products.screens.ProductDetailsActions
import com.sandoval.mercadosearch.ui.search_products.screens.SearchResultsActions
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductAttributeUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductDetailDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductPictureUIModel
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState

/*
Mock de modelos para simular los estados en los previews
 */

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

val attributes = listOf(
    ProductAttributeUIModel(name = "Color", value = "Black"),
    ProductAttributeUIModel(name = "Brand", value = "Generic"),
    ProductAttributeUIModel(name = "Weight", value = "23 kg"),
    ProductAttributeUIModel(name = "Height", value = "1 meter"),
    ProductAttributeUIModel(name = "Width", value = "3 meter")
)

val productDetails = ProductDetailDataUIModel(
    warranty = "",
    pictures = listOf(
        ProductPictureUIModel("1", ""),
        ProductPictureUIModel("1", "")
    ),
    attributes = attributes,
    availableQuantity = "3",
    availableQuantityColor = Color.Gray,
    soldQuantity = "4 sold"
)

/*
Estados de UI para la pantalla de Resultados de Busqueda
 */

val searchResultActions = SearchResultsActions(
    doWhenSearchActionClicked = {},
    doWhenLoadingMoreItems = {},
    doWhenSearchedTextChanged = {},
    doOnSelectedProduct = {},
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

/*
Estados de UI para la pantalla de Busqueda Inicial
 */

val productDetailActions =
    ProductDetailsActions(
        doWhenSharedButtonClicked = {},
        doWhenBackButtonClicked = {},
        doWhenShowFeaturesClicked = {})

val productDetailsLoading = ProductDetailState.Loading(product)
val productDetailsReady = ProductDetailState.DetailsReady(product, productDetails)
val productFailureReady = ProductDetailState.Failure(product, error)
