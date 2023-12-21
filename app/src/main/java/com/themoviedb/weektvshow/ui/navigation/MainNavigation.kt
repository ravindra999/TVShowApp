package com.themoviedb.weektvshow.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.themoviedb.weektvshow.ui.screens.details.DetailsScreen
import com.themoviedb.weektvshow.ui.screens.home.HomeScreen

@Composable
fun MainNavigation(
    paddingValues: PaddingValues
) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = NavItem.Home.baseRoute
    ) {
        composable(NavItem.Home) {
            HomeScreen(
                onCardClick = { navController.safeNavigate(NavItem.Details.createRoute(it)) }
            )
        }
        composable(NavItem.Details) {
            DetailsScreen(onBackPressed = {
                navController.popBackStack()
            },
                onCardClick = {
                    navController.navigateUp()
                    navController.safeNavigate(NavItem.Details.createRoute(it))
                })
        }
    }
}

private fun NavHostController.safeNavigate(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route = navItem.route, arguments = navItem.args) {
        content(it)
    }
}