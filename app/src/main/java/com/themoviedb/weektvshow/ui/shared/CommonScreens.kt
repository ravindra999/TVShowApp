package com.themoviedb.weektvshow.ui.shared

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.themoviedb.weektvshow.R
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun <T> PageWithState(
    uiState: UiState?,
    successBlock: @Composable (T) -> Unit
) {
    Box {
        when (uiState) {
            UiState.Loading -> {
                LoadingScreen(modifier = Modifier.background(MaterialTheme.colors.surface))
            }
            is UiState.Empty -> {
                ScreenWithMessage(message = R.string.no_results)
            }
            is UiState.Error -> {
                ErrorScreen(
                    errorMessage = uiState.errorMessage,
                    errorStringResource = uiState.errorStringResource
                )
            }
            is UiState.Success<*> -> {
                @Suppress("UNCHECKED_CAST")
                successBlock(uiState.data as T)
            }
            else -> {}
        }
    }
}
@Composable
fun ScreenWithMessage(
    modifier: Modifier = Modifier,
    message: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimatedSnackBar(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState
    ) { snackbarData ->
        AnimatedVisibility(
            visible = isConnected,
            enter = slideInVertically() + expandVertically(),
            exit = slideOutVertically() + shrinkVertically()
        ) {
            Snackbar(
                backgroundColor = Color.DarkGray
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = snackbarData.message,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String?,
    errorStringResource: Int?
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        errorMessage?.let {
            Text(
                text = it,
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
            return@Column
        }
        errorStringResource?.let {
            Text(
                text = stringResource(id = it),
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
            return@Column
        }
    }
}