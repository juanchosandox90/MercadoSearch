package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography

@Composable
fun SearchResultScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        NoResultsFoundSection()
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

@Preview(name = "NoResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenNoResultsStatePreview() {
    BuildSearchResultsPreview()
}

@Composable
fun BuildSearchResultsPreview() {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchResultScreen()
        }
    }
}
