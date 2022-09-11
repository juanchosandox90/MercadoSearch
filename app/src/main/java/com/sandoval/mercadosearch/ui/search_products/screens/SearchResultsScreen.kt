package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandoval.mercadosearch.ui.compose.ErrorSnackbarHost
import com.sandoval.mercadosearch.ui.compose.RectangleSearchTextField
import com.sandoval.mercadosearch.ui.compose.ShowErrorSnackBar
import com.sandoval.mercadosearch.ui.search_products.screens.preview.*
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography
import com.sandoval.mercadosearch.ui.viewmodel.models.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState

@Composable
fun SearchResultScreen(
    searchTextValue: TextFieldValue,
    searchState: ProductSearchState?,
    actions: SearchResultsActions
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var searchFocusState by remember { mutableStateOf(true) }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RectangleSearchTextField(
                searchTextValue,
                enabled = true,
                doWhenSearchedTextChanged = {},
                doWhenSearchButtonClicked = actions.doWhenSearchActionClicked,
                doWhenBackButtonClicked = actions.doWhenBackButtonClicked,
                doWhenFocused = { searchFocusState = true },
                doWhenFocusLost = { searchFocusState = false },
            )
        },
        snackbarHost = { snackbarHostState -> ErrorSnackbarHost(snackbarHostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            when (searchState) {
                ProductSearchState.Loading -> LoadingSection()
                ProductSearchState.NoResults -> NoResultsFoundSection()
                is ProductSearchState.Results -> {
                    ProductsListSection(
                        searchState.products
                    )
                    searchState.refreshingError?.let { error ->
                        ShowErrorSnackBar(
                            coroutineScope,
                            scaffoldState.snackbarHostState,
                            error
                        ) {
                            //TODO intentar cargar mas items
                        }
                    }
                }
                is ProductSearchState.Failure -> {
                    with(searchState.error) {
                        FailureSection(generalPurposeMessage)
                        if (!criticalMessage.isNullOrBlank()) {
                            ShowErrorSnackBar(
                                coroutineScope,
                                scaffoldState.snackbarHostState,
                                searchState.error
                            ) {
                                actions.doWhenSearchActionClicked()
                            }
                        }
                    }
                }
                else -> {}
            }
            if (searchFocusState) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {}
                )
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}

@Composable
private fun ProductsListSection(
    products: List<ProductDataUIModel>
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState
    ) {
        items(
            items = products,
            key = { product -> product.id }) { product ->
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = product.name
                    )
                }
            }
        }
    }

}

@Composable
private fun NoResultsFoundSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))
        Text(
            text = "No se encontraron resultados",
            style = Typography.h5
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "Intenta con otras palabras clave"
        )
    }
}

@Composable
private fun FailureSection(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TODO Colocar una imagen de error
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = message,
            style = Typography.h6
        )
    }
}

data class SearchResultsActions(
    val doWhenSearchActionClicked: () -> Unit,
    val doWhenBackButtonClicked: () -> Unit
)

@Preview(name = "LoadingStateState")
@Composable
fun SearchResultsScreenLoadingStateStatePreview() {
    BuildSearchResultsPreview(searchResultsLoading)
}

@Preview(name = "ResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenResultsStatePreview() {
    BuildSearchResultsPreview(searchResults)
}

@Preview(name = "NoResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenNoResultsStatePreview() {
    BuildSearchResultsPreview(searchResultsNoResults)
}

@Preview(name = "ResultsFailureState")
@Composable
fun SearchResultsScreenResultsFailureStatePreview() {
    BuildSearchResultsPreview(searchResultsRefreshingError)
}

@Preview(name = "FailureState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenFailureStatePreview() {
    BuildSearchResultsPreview(searchResultsFailure)
}

@Composable
fun BuildSearchResultsPreview(state: ProductSearchState) {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchResultScreen(
                searchTextValue = TextFieldValue(),
                actions = searchResultActions,
                searchState = state
            )
        }
    }
}
