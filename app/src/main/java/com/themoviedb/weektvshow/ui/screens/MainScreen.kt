package com.themoviedb.weektvshow.ui.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.themoviedb.weektvshow.R
import com.themoviedb.weektvshow.ui.navigation.MainNavigation
import com.themoviedb.weektvshow.ui.shared.AnimatedSnackBar
import com.themoviedb.weektvshow.ui.shared.getNetworkStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MainScreen() {
    val snackbarHostState = SnackbarHostState()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        snackbarHost = { ShowNetworkStatusSnackBar(snackbarHostState = it) }
    ) {
        MainNavigation(it)
    }
}

@ExperimentalCoroutinesApi
@Composable
fun ShowNetworkStatusSnackBar(snackbarHostState: SnackbarHostState) {

    if (getNetworkStatus()) {
        snackbarHostState.currentSnackbarData?.dismiss()

    } else {
        val message = stringResource(id = R.string.error_no_internet)
        LaunchedEffect(true) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "",
                duration = SnackbarDuration.Indefinite
            )
        }

        AnimatedSnackBar(
            isConnected = true,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun MainScreenPreview() {
    MainScreen()
}