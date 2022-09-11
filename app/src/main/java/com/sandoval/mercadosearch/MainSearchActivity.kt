package com.sandoval.mercadosearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sandoval.mercadosearch.ui.search_products.router.MercadoSearchNavigation
import com.sandoval.mercadosearch.ui.search_products.router.MercadoSearchNavigationActions
import com.sandoval.mercadosearch.ui.search_products.screens.preview.product
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.utils.shareText
import com.sandoval.mercadosearch.ui.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainSearchActivity : ComponentActivity() {

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MercadoSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MercadoSearchNavigation(
                        searchState = viewModel.searchState.observeAsState(),
                        detailsState = viewModel.detailsState.observeAsState(),
                        mercadoSearchNavigationActions = setupActions()
                    )
                }
            }
        }
    }

    private fun setupActions(): MercadoSearchNavigationActions = MercadoSearchNavigationActions(
        doWhenSearchActionClicked = { text -> viewModel.initialSearch(text) },
        doWhenShowProductDetails = { product -> viewModel.getProductDetails(product) },
        doWhenSharedButtonClicked = { description ->
            shareText(title = "Mercado Search App shared product", description)
        },
        doWhenBackButtonPressed = { onBackPressed() }
    )
}