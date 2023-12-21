package com.themoviedb.weektvshow.data.source.remote

import com.themoviedb.weektvshow.data.Result
import com.themoviedb.weektvshow.data.network.TVShowApiService
import com.themoviedb.weektvshow.data.network.models.ErrorResponse
import com.themoviedb.weektvshow.data.network.models.tvshowdetails.TvShowDetails
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShowsResponse
import com.themoviedb.weektvshow.data.source.mapResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TvShowDetailsRemoteDataSource {
    fun getTvShowDetails(id: Int): Flow<Result<TvShowDetails, ErrorResponse>>
    fun getSimilarTVShowsById(id: Int): Flow<Result<TvShowsResponse, ErrorResponse>>
}

class TvShowDetailsRemoteDataSourceImp @Inject constructor(
    private val apiService: TVShowApiService
) : TvShowDetailsRemoteDataSource {

    override fun getTvShowDetails(id: Int): Flow<Result<TvShowDetails, ErrorResponse>> =
        mapResponse(Dispatchers.IO) {
            apiService.getTVShowById(id)
        }

    override fun getSimilarTVShowsById(id: Int): Flow<Result<TvShowsResponse, ErrorResponse>> =
        mapResponse(Dispatchers.IO) {
            apiService.getSimilarTVShowsById(id)
        }
}