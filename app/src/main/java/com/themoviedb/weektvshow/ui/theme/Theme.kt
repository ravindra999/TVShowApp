package com.themoviedb.weektvshow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Orange200,
    primaryVariant = Red700,
    secondary = Teal200,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = Orange500,
    primaryVariant = Red700,
    secondary = Teal200,
    onBackground = Color.DarkGray
)

@Composable
fun TvShowAppTheme(
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
