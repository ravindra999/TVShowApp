package com.themoviedb.weektvshow.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.themoviedb.weektvshow.utils.Constants.HOME_SCREEN
import com.themoviedb.weektvshow.utils.Constants.MAIN_SCREEN

sealed class NavItem(
    val baseRoute: String,
    private val navArgs: List<NavArgs> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute).plus(argKeys).joinToString("/")
    }

    val args = navArgs.map { navArgument(name = it.key) { type = it.navType } }

    //navigation objects
    object Main : NavItem(MAIN_SCREEN)

    //main navigation
    object Home : NavItem(HOME_SCREEN)
 }

enum class NavArgs(
    val key: String,
    val navType: NavType<*>
)