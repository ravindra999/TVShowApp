package com.themoviedb.weektvshow.data.network

import com.themoviedb.weektvshow.BuildConfig
import com.themoviedb.weektvshow.data.network.models.ErrorResponse
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShowsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVShowApiService {
    //tv show trending api call
    @GET("trending/tv/{week}")
    suspend fun getTrendingTvShows(
        @Path("week") week: String = "week",
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<TvShowsResponse, ErrorResponse>

}