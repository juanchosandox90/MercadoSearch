package com.sandoval.mercadosearch.ui.compose

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.sandoval.mercadosearch.ui.base.ErrorUIModel
import com.sandoval.mercadosearch.ui.theme.MercadoSearchAmaranthRed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Error

@Composable
fun ErrorSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Snackbar(
            contentColor = Color.White,
            actionColor = Color.White,
            backgroundColor = MercadoSearchAmaranthRed,
            snackbarData = snackbarData
        )
    }
}

@Composable
fun ShowErrorSnackBar(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    error: ErrorUIModel,
    doWhenActionPerformed: () -> Unit
) {
    LaunchedEffect(
        key1 = snackbarHostState,
        block = {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = error.getWarningMessage(),
                    actionLabel = error.suggestedAction,
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    doWhenActionPerformed()
                }
            }
        }
    )
}