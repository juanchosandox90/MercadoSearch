package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sandoval.mercadosearch.R
import com.sandoval.mercadosearch.ui.compose.ErrorSnackbarHost
import com.sandoval.mercadosearch.ui.compose.RectangleSearchTextField
import com.sandoval.mercadosearch.ui.compose.ShowErrorSnackBar
import com.sandoval.mercadosearch.ui.search_products.screens.preview.*
import com.sandoval.mercadosearch.ui.theme.MercadoSearchGreenHaze
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

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
                enabled = searchState != ProductSearchState.Loading,
                doWhenSearchedTextChanged = actions.doWhenSearchedTextChanged,
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
                        searchState.products,
                        searchState.isRefreshing,
                        actions.doWhenLoadingMoreItems,
                        actions.doOnSelectedProduct
                    )
                    searchState.refreshingError?.let { error ->
                        ShowErrorSnackBar(
                            coroutineScope,
                            scaffoldState.snackbarHostState,
                            error
                        ) {
                            actions.doWhenLoadingMoreItems()
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
            when {
                searchFocusState -> {
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
    products: List<ProductDataUIModel>,
    isRefreshing: Boolean,
    doWhenLoadingMoreItems: () -> Unit,
    doOnSelectedProduct: (ProductDataUIModel) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState
    ) {
        items(
            items = products,
            key = { product -> product.id }) { product ->
            Row(
                modifier = Modifier.clickable { doOnSelectedProduct(product) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.picture)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_no_picture_256)
                        .error(R.drawable.ic_no_picture_256)
                        .build(),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(60.dp)
                )
                Column {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = product.name
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                        text = product.price,
                        style = Typography.h6
                    )
                    when {
                        product.freeShipping -> {
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    top = 4.dp,
                                    bottom = 16.dp
                                ),
                                text = "Free Shipping",
                                style = Typography.caption,
                                color = MercadoSearchGreenHaze
                            )
                        }
                        else -> {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    Divider()
                }
            }
        }
        if (isRefreshing) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center,
                )
                {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            }
        }
    }
    DetermineIfLoadMoreItems(isRefreshing, lazyListState) {
        doWhenLoadingMoreItems()
    }
}

/**
 *
 * Determinar si la lista debe solicitar más datos. Funciona comprobando si el primer elemento
 * del [threshold] es visible, esto se hace usando un [derivedStateOf] para evitar
 * recomposiciones innecesarias. Tiene en cuenta si la UI ya está esperando más
 * datos ([isRefreshing]) para evitar generar señales verdaderas en este caso.
 *
 *
 * Un [LaunchedEffect] reside aquí junto con un [snapshotFlow] para detectar y notificar
 * el resultado de la validación mencionada anteriormente, estos efectos se lanzan durante la primera
 * composición y disposición cuando esta función deja la composición. El cómputo realizado
 * a través de estos efectos, básicamente escucha el estado generado por [derivedStateOf] y convierte
 * como flujo para activar [doWhenLoadingMoreItems] evitando invocarlo varias veces.
 *
 * @param isRefreshing Notifica si la UI ya fue refrescada
 * @param listState Estado de la lista a validar
 * @param doWhenLoadingMoreItems
 * @param threshold Numero de items que indica en que punto la UI debe ser llenada de items
 *
 * */

@Composable
fun DetermineIfLoadMoreItems(
    isRefreshing: Boolean,
    listState: LazyListState,
    threshold: Int = 5,
    doWhenLoadingMoreItems: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            if (lastVisibleItem != null) {
                isRefreshing.not() && lastVisibleItem >= listState.layoutInfo.totalItemsCount - threshold
            } else {
                false
            }
        }
    }
    LaunchedEffect(true) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (isRefreshing.not()) {
                    doWhenLoadingMoreItems()
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
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))
        Image(
            painter = painterResource(id = R.drawable.ic_error_exclamation_256),
            contentDescription = "Advertencia"
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = message,
            style = Typography.h6
        )
    }
}

data class SearchResultsActions(
    val doWhenSearchActionClicked: () -> Unit,
    val doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    val doWhenLoadingMoreItems: () -> Unit,
    val doWhenBackButtonClicked: () -> Unit,
    val doOnSelectedProduct: (ProductDataUIModel) -> Unit
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
