package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandoval.mercadosearch.ui.compose.BackButton
import com.sandoval.mercadosearch.ui.search_products.screens.preview.product
import com.sandoval.mercadosearch.ui.search_products.screens.preview.productDetails
import com.sandoval.mercadosearch.ui.theme.MercadoSearchBrightGray
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography
import com.sandoval.mercadosearch.ui.viewmodel.models.details_product.ProductAttributeUIModel
import com.sandoval.mercadosearch.ui.viewmodel.state.ProductDetailState

@Composable
fun ProductFeaturesScreen(
    state: ProductDetailState?,
    doWhenBackButtonClicked: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar {
            TopAppBar(title = { Text(text = "Caracteristicas del Producto") },
                navigationIcon = {
                    BackButton(doWhenBackButtonClicked = doWhenBackButtonClicked)
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.Black,
                elevation = 10.dp
            )
        }
    }) { paddingValues ->
        if (state != null && state is ProductDetailState.DetailsReady) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(bottom = paddingValues.calculateBottomPadding()),
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "Especificaciones",
                        style = Typography.h6
                    )
                }
                itemsIndexed(state.details.attributes) { index, attribute ->
                    FeatureItem(
                        index = index,
                        isLastItem = index == state.details.attributes.lastIndex,
                        attribute = attribute
                    )
                }
            }
        }

    }
}

@Composable
fun FeatureItem(
    index: Int,
    isLastItem: Boolean,
    attribute: ProductAttributeUIModel
) {
    val isFirstItem = index == 0
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp, end = 16.dp,
                bottom = if (isLastItem) 16.dp else 0.dp
            )
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        if (isFirstItem) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
        Row(
            modifier = Modifier
                .background(if (index % 2 == 0) Color.White else MercadoSearchBrightGray)
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                text = attribute.name,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp),
                text = attribute.value
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        }
        if (isLastItem) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}

@Preview("Portrait")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun ProductFeaturesScreenPreview() {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ProductFeaturesScreen(state =
            ProductDetailState.DetailsReady(
                product = product,
                details = productDetails
            ), doWhenBackButtonClicked = {})
        }
    }
}