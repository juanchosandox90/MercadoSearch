package com.sandoval.mercadosearch.ui.search_products.router

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sandoval.mercadosearch.ui.theme.MercadoSearchYellow
import com.sandoval.mercadosearch.ui.compose.textFieldSaver
import com.sandoval.mercadosearch.ui.search_products.screens.*
import com.sandoval.mercadosearch.ui.viewmodel.models.products.ProductDataUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductSearchState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MercadoSearchNavigation(
    searchState: State<ProductSearchState?>,
    detailsState: State<ProductDetailState?>,
    mercadoSearchNavigationActions: MercadoSearchNavigationActions
) {

    val navigationController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()

    val springSpec = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )

    var searchTextValue by rememberSaveable(stateSaver = textFieldSaver) {
        mutableStateOf(TextFieldValue())
    }

    AnimatedNavHost(navigationController, startDestination = Route.SEARCH.name) {
        composable(Route.SEARCH.name) {
            SetStatusBarColor(systemUiController = systemUiController, MercadoSearchYellow)
            SearchProductScreen(
                searchTextValue = searchTextValue,
                doWhenSearchedTextChanged = { text -> searchTextValue = text },
                doWhenSearchActionClicked = {
                    navigationController.navigate(Route.RESULTS.name)
                    mercadoSearchNavigationActions.doWhenSearchActionClicked(searchTextValue.text)
                }
            )
        }

        composable(Route.RESULTS.name,
            enterTransition = {
                slideInVertically(initialOffsetY = { 1000 }, animationSpec = springSpec)
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { 2000 }, animationSpec = springSpec)
            }
        ) {
            SetStatusBarColor(systemUiController = systemUiController, color = Color.White)
            SearchResultScreen(
                searchTextValue = searchTextValue,
                searchState = searchState.value,
                actions = searchResultActions(
                    searchTextValue,
                    doWhenSearchedTextChanged = { text ->
                        searchTextValue = text
                    },
                    mercadoSearchNavigationActions,
                    navigationController
                )
            )
        }

        composable(Route.DETAILS.name) {
            SetStatusBarColor(systemUiController = systemUiController, color = MercadoSearchYellow)
            ProductDetailScreen(
                state = detailsState.value,
                actions = productDetailsActions(
                    mercadoSearchNavigationActions
                )
            )
        }
    }
}

@Composable
private fun searchResultActions(
    searchTextValue: TextFieldValue,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    mercadoSearchNavigation: MercadoSearchNavigationActions,
    navigationController: NavHostController
) = SearchResultsActions(
    doWhenSearchedTextChanged = doWhenSearchedTextChanged,
    doWhenSearchActionClicked = {
        mercadoSearchNavigation.doWhenSearchActionClicked(
            searchTextValue.text
        )
    },
    doOnSelectedProduct = { product ->
        mercadoSearchNavigation.doWhenShowProductDetails(product)
        navigationController.navigate(Route.DETAILS.name)
    },
    doWhenBackButtonClicked = mercadoSearchNavigation.doWhenBackButtonPressed
)

@Composable
private fun productDetailsActions(
    mercadoSearchNavigation: MercadoSearchNavigationActions
) =
    ProductDetailsActions(
        doWhenSharedButtonClicked = { description ->
            mercadoSearchNavigation.doWhenSharedButtonClicked(description)
        },
        doWhenBackButtonClicked = {
            mercadoSearchNavigation.doWhenBackButtonPressed()
        }
    )


@Composable
private fun SetStatusBarColor(systemUiController: SystemUiController, color: Color) {
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
}

data class MercadoSearchNavigationActions(
    val doWhenSearchActionClicked: (String) -> Unit,
    val doWhenShowProductDetails: (ProductDataUIModel) -> Unit,
    val doWhenSharedButtonClicked: (String) -> Unit,
    val doWhenBackButtonPressed: () -> Unit
)

enum class Route {
    SEARCH, RESULTS, DETAILS, FEATURES
}