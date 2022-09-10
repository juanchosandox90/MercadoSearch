package com.sandoval.mercadosearch.ui.search_products.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandoval.mercadosearch.ui.compose.RoundedSearchTextField
import com.sandoval.mercadosearch.ui.theme.*

@Composable
fun SearchProductScreen(
    searchTextValue: TextFieldValue,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    doWhenSearchActionClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MercadoSearchYellow)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(getMaxContentWidth()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Bienvenido a Mercado Search!",
                style = Typography.h5
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                text = "Compra todo lo que necesites al mejor precio"
            )
            RoundedSearchTextField(
                padding = PaddingValues(top = 32.dp),
                searchTextValue = searchTextValue,
                doWhenSearchedTextChanged = doWhenSearchedTextChanged,
                doWhenSearchActionClicked = doWhenSearchActionClicked
            )
            val isSearchEnabled = searchTextValue.text.isBlank().not()

            IconButton(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .background(
                        color = if (isSearchEnabled) MercadoSearchBrightGray else MercadoSearchDarkGray,
                        shape = CircleShape
                    ),
                enabled = isSearchEnabled,
                onClick = {
                    focusManager.clearFocus(force = true)
                    doWhenSearchActionClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MercadoSearchBlue,
                    contentDescription = "Search icon"
                )
            }
        }
    }
}

/**
 * Retorna el porcentaje de la pantalla que representa el maximo ancho
 * del componente composable padre que contiene todos los widgets dentro
 * de la jerarquia. La configuracion actual retorna asi:
 *
 * 65% Para orientacion landscape
 * 85% Para orientacion vertical
 *
 * @return percentage
 */

@Composable
private fun getMaxContentWidth(): Float {
    val localConfiguration = LocalConfiguration.current
    return if (localConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        0.65f
    } else {
        0.85f
    }
}

@Preview(name = "Portrait")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchProductScreenPreviewPortrait() {
    MercadoSearchTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchProductScreen(searchTextValue = TextFieldValue(),
                doWhenSearchedTextChanged = {},
                doWhenSearchActionClicked = {})
        }
    }
}