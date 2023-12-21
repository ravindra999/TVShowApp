package com.themoviedb.weektvshow.data.source

import com.themoviedb.weektvshow.data.Result
import com.themoviedb.weektvshow.data.network.models.ErrorResponse
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShowsResponse
import com.themoviedb.weektvshow.data.network.models.tvshowdetails.TvShowDetails
import com.themoviedb.weektvshow.data.room.entities.FavoriteTvShow
import com.themoviedb.weektvshow.data.source.local.TvShowDetailsLocalDataSource
import com.themoviedb.weektvshow.data.source.remote.TvShowDetailsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface TvShowDetailsRepository {
    fun getTvShowDetails(id: Int): Flow<Result<TvShowDetails, ErrorResponse>>
    fun getSimilarTVShowsById(id: Int): Flow<Result<TvShowsResponse, ErrorResponse>>

    //local management methods
    fun getFavsTvShows(): Flow<List<FavoriteTvShow>>
    suspend fun insertFavTvShow(tvShow: FavoriteTvShow)
    fun getFavId(id: Int): Flow<Int?>
    suspend fun deleteFavTvShow(id: Int)
}

class TvShowDetailsRepositoryImp @Inject constructor(
    private val tvShowDetailsRemoteDataSource: TvShowDetailsRemoteDataSource,
    private val tvShowDetailsLocalDataSource: TvShowDetailsLocalDataSource
) : TvShowDetailsRepository {

    //remote
    override fun getTvShowDetails(id: Int): Flow<Result<TvShowDetails, ErrorResponse>> {
        return tvShowDetailsRemoteDataSource.getTvShowDetails(id).flowOn(Dispatchers.Default)
    }

    override fun getSimilarTVShowsById(id: Int): Flow<Result<TvShowsResponse, ErrorResponse>> {
        return tvShowDetailsRemoteDataSource.getSimilarTVShowsById(id).flowOn(Dispatchers.Default)
    }

    //local
    override fun getFavsTvShows(): Flow<List<FavoriteTvShow>> {
        return tvShowDetailsLocalDataSource.getFavsTvShows()
    }

    override suspend fun insertFavTvShow(tvShow: FavoriteTvShow) {
        tvShowDetailsLocalDataSource.insertFavTvShow(tvShow)
    }

    override fun getFavId(id: Int): Flow<Int?> {
        return tvShowDetailsLocalDataSource.getFavId(id)
    }

    override suspend fun deleteFavTvShow(id: Int) {
        tvShowDetailsLocalDataSource.deleteFavTvShow(id)
    }

}