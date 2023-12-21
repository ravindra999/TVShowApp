package com.themoviedb.weektvshow.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowSize(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return WindowSizeClass(
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> WindowSizeClass.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowSizeClass.WindowType.Medium
            else -> WindowSizeClass.WindowType.Expanded
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> WindowSizeClass.WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowSizeClass.WindowType.Medium
            else -> WindowSizeClass.WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class WindowSizeClass(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class WindowType {
        object Compact : WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
}