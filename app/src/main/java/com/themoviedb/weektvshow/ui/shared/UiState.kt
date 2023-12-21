package com.themoviedb.weektvshow.ui.shared

sealed interface UiState {
    data class Success<out T>(val data: T) : UiState
    data class Error(
        val errorMessage: String? = null, val errorStringResource: Int? = null
    ) : UiState

    object Loading : UiState
    object Empty : UiState
}