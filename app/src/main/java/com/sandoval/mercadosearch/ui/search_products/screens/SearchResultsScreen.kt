package com.sandoval.mercadosearch.ui.search_products.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandoval.mercadosearch.ui.compose.RectangleSearchTextField
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import com.sandoval.mercadosearch.ui.theme.Typography

@Composable
fun SearchResultScreen(
    searchTextValue: TextFieldValue,
    actions: SearchResultsActions
) {
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
                doWhenSearchButtonClicked = { /*TODO*/ },
                doWhenBackButtonClicked = actions.doWhenBackButtonClicked,
                doWhenFocused = { searchFocusState = true },
                doWhenFocusLost = { searchFocusState = false },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
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
            NoResultsFoundSection()
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

data class SearchResultsActions(
    val doWhenBackButtonClicked: () -> Unit
)

@Preview(name = "NoResultsState")
@Preview(name = "Landscape", device = Devices.PIXEL_4_XL, widthDp = 720, heightDp = 360)
@Composable
fun SearchResultsScreenNoResultsStatePreview() {
    BuildSearchResultsPreview()
}

//TODO: Estos estados de previsualizacion podria migrarlos a un archivo independiente
val searchResultActions = SearchResultsActions(
    doWhenBackButtonClicked = {})

@Composable
fun BuildSearchResultsPreview() {
    MercadoSearchTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SearchResultScreen(
                searchTextValue = TextFieldValue(),
                actions = searchResultActions,
            )
        }
    }
}
