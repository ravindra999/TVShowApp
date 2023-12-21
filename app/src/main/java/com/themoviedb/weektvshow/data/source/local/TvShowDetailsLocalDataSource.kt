package com.themoviedb.weektvshow.data.source.local

import com.themoviedb.weektvshow.data.room.daos.FavsTvShowsDao
import com.themoviedb.weektvshow.data.room.entities.FavoriteTvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TvShowDetailsLocalDataSource {
    fun getFavsTvShows(): Flow<List<FavoriteTvShow>>
    suspend fun insertFavTvShow(tvShow: FavoriteTvShow)
    fun getFavId(id: Int): Flow<Int?>
    suspend fun deleteFavTvShow(id: Int)
}

class TvShowDetailsLocalDataSourceImp @Inject constructor(
    private val favsTvShowsDao: FavsTvShowsDao
) : TvShowDetailsLocalDataSource {

    override fun getFavsTvShows(): Flow<List<FavoriteTvShow>> = flow {
        emit(favsTvShowsDao.getFavsTvShows())
    }.flowOn(Dispatchers.IO)

    override suspend fun insertFavTvShow(tvShow: FavoriteTvShow) = withContext(Dispatchers.IO) {
        favsTvShowsDao.insertFavTvShow(tvShow)
    }

    override fun getFavId(id: Int): Flow<Int?> = flow {
        emit(favsTvShowsDao.getFavId(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFavTvShow(id: Int) = withContext(Dispatchers.IO) {
        favsTvShowsDao.deleteFavTvShow(id)
    }

}

