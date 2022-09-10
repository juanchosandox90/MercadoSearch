package com.sandoval.mercadosearch.ui.compose

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun BackButton(
    doWhenBackButtonClicked: () -> Unit,
    enabled: Boolean = true
) {
    IconButton(
        onClick = { doWhenBackButtonClicked() },
        enabled = enabled
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back arrow")
    }
}