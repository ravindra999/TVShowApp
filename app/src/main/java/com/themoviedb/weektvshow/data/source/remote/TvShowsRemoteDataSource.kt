package com.themoviedb.weektvshow.data.source.remote

import com.themoviedb.weektvshow.data.Result
import com.themoviedb.weektvshow.data.network.TVShowApiService
import com.themoviedb.weektvshow.data.network.models.ErrorResponse
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShowsResponse
import com.themoviedb.weektvshow.data.source.mapResponse
import javax.inject.Inject

interface TvShowsRemoteDataSource {
    suspend fun getTrendingTvShows(): Result<TvShowsResponse, ErrorResponse>
}

class TvShowsRemoteDataSourceImp @Inject constructor(
    private val apiService: TVShowApiService
) : TvShowsRemoteDataSource {

    override suspend fun getTrendingTvShows() = mapResponse {
        apiService.getTrendingTvShows()
    }
}