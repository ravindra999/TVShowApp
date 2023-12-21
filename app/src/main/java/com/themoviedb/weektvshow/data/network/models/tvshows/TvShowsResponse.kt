package com.themoviedb.weektvshow.data.network.models.tvshows

data class TvShowsResponse(
    val page: Int,
    val results: List<TvShow>,
    val total_pages: Int,
    val total_results: Int
)