package com.themoviedb.weektvshow.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.source.TvShowsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {
    private val _tvShows = MutableStateFlow<PagingData<TvShow>>(PagingData.empty())
    val tvShows = _tvShows.asStateFlow()
    init {
        getTredingTvShows()
    }


    fun getTredingTvShows() = viewModelScope.launch {
        repository.getTrendingTvShows()
            .cachedIn(viewModelScope)
            .collectLatest { tvShows ->
                _tvShows.update { tvShows }
            }
    }


    fun getLocalListOfTvShows() = viewModelScope.launch {

        repository.getCachedTvShows().collect {
            val tvShows = it ?: emptyList()

            _tvShows.update { PagingData.from(tvShows) }
        }
    }

    fun cacheTvShows(tvShow: TvShow) = viewModelScope.launch {
        var oldestTimestamp = System.currentTimeMillis()

        repository.getCachedTvShows().collect { tvShowList ->
            tvShowList?.let {
                if (tvShowList.isNotEmpty()) {
                    oldestTimestamp = it.last().timestamp
                }
            }

            val needsRefresh =
                oldestTimestamp < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(20)
                        || tvShowList.isNullOrEmpty()

            if (needsRefresh) {
                repository.clearCachedTvShows()
                repository.insertTvShows(tvShow)
                return@collect
            }
        }
    }
}