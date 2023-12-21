package com.themoviedb.weektvshow.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimen(

    //widgets dimensions
    val default: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val smallMedium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val mediumLarge: Dp = 40.dp,
    val extraLarge: Dp = 64.dp,
    val extraExtraLarge: Dp = 128.dp,
    val mediumButtonSize: Dp = 48.dp,
    //border and elevation
    val borderThing: Dp = 1.dp,
    val borderMedium: Dp = 4.dp,
    val borderRounded: Dp = 10.dp,
    val elevationDefault: Dp = 4.dp,
    val elevationNormal: Dp = 10.dp,
    //rounder corner shapes
    val roundedMedium: Dp = 10.dp,
    //appbar size
    val appBarNormal: Dp = 48.dp,
    val appBarLarge: Dp = 96.dp,
    //images
    val imageDefault: Dp = 24.dp,
    val imageSmall: Dp = 32.dp,
    val imageMedium: Dp = 96.dp,
    val imageLarge: Dp = 144.dp,
    val imageExtraLarge: Dp = 200.dp
)

val LocalDimen = compositionLocalOf { Dimen() }

val MaterialTheme.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current