package com.themoviedb.weektvshow.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themoviedb.weektvshow.R
import com.themoviedb.weektvshow.data.Result
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.room.entities.FavoriteTvShow
import com.themoviedb.weektvshow.data.source.TvShowDetailsRepository
import com.themoviedb.weektvshow.ui.shared.UiState
import com.themoviedb.weektvshow.utils.Constants.TV_SHOW_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TvShowDetailsRepository
) : ViewModel() {
    private val _tvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val tvShows = _tvShows.asStateFlow()
    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState = _uiState.asStateFlow()

    private val _isInFavorites = MutableStateFlow(false)
    val isInFavorites = _isInFavorites.asStateFlow()

    init {
        savedStateHandle.get<Int>(TV_SHOW_ID_ARGUMENT)?.let { tvShowId ->
            getTvShowDetails(tvShowId)
            getSimilarTVShowsById(tvShowId)
            tvShowIsInFavorites(tvShowId)
        }
    }

    fun getTvShowDetails(id: Int) = viewModelScope.launch {
        repository.getTvShowDetails(id).collect { result ->
            when (result) {
                is Result.Error -> _uiState.update { UiState.Error(errorStringResource = R.string.no_info_about_tv_show) }
                Result.Loading -> _uiState.update { UiState.Loading }
                is Result.Success -> _uiState.update {
                    UiState.Success(result.data)
                }
            }
        }
    }

    private fun getSimilarTVShowsById(id: Int) = viewModelScope.launch {
        repository.getSimilarTVShowsById(id).collect { result ->
            when (result) {
                is Result.Error -> _uiState.update { UiState.Error(errorStringResource = R.string.no_info_about_tv_show) }
                Result.Loading -> _uiState.update { UiState.Loading }
                is Result.Success ->  _tvShows.update { result.data?.results!!.filter {it.backdrop_path?.isNotEmpty()==true  } }
            }
        }
    }

    fun tvShowIsInFavorites(id: Int) = viewModelScope.launch {
        repository.getFavId(id).collect { dbId ->
            when (dbId) {
                id -> _isInFavorites.update { true }
                null -> _isInFavorites.update { false }
            }
        }
    }

    fun insertFavoriteTvShow(tvShow: FavoriteTvShow) = viewModelScope.launch {
        repository.insertFavTvShow(tvShow)
    }

    fun deleteFavoriteTvShow(id: Int) = viewModelScope.launch {
        repository.deleteFavTvShow(id)
    }
}