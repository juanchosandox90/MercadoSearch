package com.sandoval.mercadosearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = MercadoSearchYellow,
    primaryVariant = MercadoSearchYellow,
    secondary = MercadoSearchDarkBlue
)

private val LightColorPalette = lightColors(
    primary = MercadoSearchYellow,
    primaryVariant = MercadoSearchYellow,
    secondary = MercadoSearchDarkBlue
)

@Composable
fun MercadoSearchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}