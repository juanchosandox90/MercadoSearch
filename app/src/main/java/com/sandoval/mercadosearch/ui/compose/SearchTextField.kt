package com.sandoval.mercadosearch.ui.compose

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RectangleSearchTextField(
    searchTextValue: TextFieldValue,
    enabled: Boolean,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    doWhenSearchButtonClicked: () -> Unit,
    doWhenBackButtonClicked: () -> Unit,
    doWhenFocused: (() -> Unit)? = null,
    doWhenFocusLost: (() -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchTextField(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RectangleShape),
        enabled = enabled,
        lockContent = true,
        shape = RectangleShape,
        searchTextValue = searchTextValue,
        leadingIcon = {
            BackButton(
                doWhenBackButtonClicked = {
                    keyboardController?.hide()
                    doWhenBackButtonClicked()
                }, enabled = enabled
            )
        },
        doWhenSearchedTextChanged = doWhenSearchedTextChanged,
        doWhenSearchActionClicked = doWhenSearchButtonClicked,
        doWhenFocused = doWhenFocused,
        doWhenFocusLost = doWhenFocusLost
    )
}

@Composable
fun RoundedSearchTextField(
    padding: PaddingValues = PaddingValues(),
    searchTextValue: TextFieldValue,
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    doWhenSearchActionClicked: () -> Unit
) {
    SearchTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(percent = 50)),
        searchTextValue = searchTextValue,
        shape = RoundedCornerShape(percent = 50),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        doWhenSearchedTextChanged = doWhenSearchedTextChanged,
        doWhenSearchActionClicked = doWhenSearchActionClicked
    )
}

@Composable
fun SearchTextField(
    modifier: Modifier,
    searchTextValue: TextFieldValue,
    shape: Shape,
    leadingIcon: @Composable (() -> Unit),
    doWhenSearchedTextChanged: (TextFieldValue) -> Unit,
    doWhenSearchActionClicked: () -> Unit,
    enabled: Boolean = true,
    lockContent: Boolean = false,
    doWhenFocused: (() -> Unit)? = null,
    doWhenFocusLost: (() -> Unit)? = null,
) {

    val focusManager = LocalFocusManager.current
    val searchTextFocusRequester = remember { FocusRequester() }
    var focusState by remember { mutableStateOf(true) }
    var requestedNewContent by remember { mutableStateOf(false) }
    val previousValue by remember { mutableStateOf(searchTextValue.text) }
    TextField(modifier = modifier.then(
        Modifier
            .focusable()
            .focusRequester(searchTextFocusRequester)
            .onFocusChanged { state ->
                if (state.hasFocus) {
                    doWhenFocused?.invoke()
                } else {
                    if (lockContent && requestedNewContent.not()
                        && (searchTextValue.text.isBlank() || searchTextValue.text != previousValue)
                    ) {
                        doWhenSearchedTextChanged(TextFieldValue(previousValue))
                    }
                    doWhenFocusLost?.invoke()
                }
                focusState = state.hasFocus
            }
    ),
        enabled = enabled,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        value = searchTextValue,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if (searchTextValue.text.isBlank().not()) {
                requestedNewContent = true
                doWhenSearchActionClicked()
            }
            focusManager.clearFocus(force = true)
        }),
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (focusState) {
                if (searchTextValue.text.isBlank().not()) {
                    IconButton(onClick = {
                        doWhenSearchedTextChanged(TextFieldValue())
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear text"
                        )
                    }
                }
            }
        },
        placeholder = {
            Text(
                text = "Busca un producto",
                maxLines = 1
            )
        },
        shape = shape,
        onValueChange = { text -> doWhenSearchedTextChanged(text) })
}