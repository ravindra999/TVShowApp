package com.themoviedb.weektvshow.data.network

import com.themoviedb.weektvshow.BuildConfig
import com.themoviedb.weektvshow.data.network.models.ErrorResponse
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShowsResponse
import com.themoviedb.weektvshow.data.network.models.tvshowdetails.TvShowDetails
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

    //tv show search api call
    @GET("search/tv")
    suspend fun getSearchQueryTVShows(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<TvShowsResponse, ErrorResponse>


    //similar tv shows api call
    @GET("tv/{id}/similar")
    suspend fun getSimilarTVShowsById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<TvShowsResponse, ErrorResponse>

    //tv show details api call
    @GET("tv/{id}")
    suspend fun getTVShowById(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): NetworkResponse<TvShowDetails, ErrorResponse>
}