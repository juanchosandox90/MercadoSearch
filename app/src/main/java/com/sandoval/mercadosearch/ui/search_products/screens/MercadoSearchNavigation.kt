package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sandoval.mercadosearch.ui.theme.MercadoSearchYellow
import com.sandoval.mercadosearch.ui.compose.textFieldSaver

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MercadoSearchNavigation() {

    val navigationController = rememberAnimatedNavController()
    val systemUiController = rememberSystemUiController()

    var searchTextValue by rememberSaveable(stateSaver = textFieldSaver) {
        mutableStateOf(TextFieldValue())
    }

    AnimatedNavHost(navigationController, startDestination = Route.SEARCH.name) {
        composable(Route.SEARCH.name) {
            SetStatusBarColor(systemUiController = systemUiController, MercadoSearchYellow)
            SearchProductScreen(
                searchTextValue = searchTextValue
            )
        }
    }
}

@Composable
private fun SetStatusBarColor(systemUiController: SystemUiController, color: Color) {
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
}

enum class Route {
    SEARCH, RESULTS, DETAILS, FEATURES
}