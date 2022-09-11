package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.sandoval.mercadosearch.ui.compose.BackButton
import com.sandoval.mercadosearch.ui.compose.ErrorSnackbarHost
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productDetailActions
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme

@Composable
fun ProductDetailScreen(
    actions: ProductDetailsActions
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {},
                actions = { TopBarDetailScreenSection(actions) },
                navigationIcon = { BackButton(actions.doWhenBackButtonClicked) }
            )
        },
        snackbarHost = { snackbarHostState -> ErrorSnackbarHost(snackbarHostState = snackbarHostState) }
    ) { paddingValues ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {

            }
        }
    }
}

@Composable
private fun TopBarDetailScreenSection(actions: ProductDetailsActions) {
    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Compartir",
            tint = Color.White
        )
    }
}

data class ProductDetailsActions(
    val doWhenBackButtonClicked: () -> Unit
)

@Preview(name = "ScreenState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun ProductsDetailsScreenPreview() {
    BuildProductDetailsPreview()
}

@Composable
fun BuildProductDetailsPreview() {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ProductDetailScreen(
                productDetailActions
            )
        }
    }
}