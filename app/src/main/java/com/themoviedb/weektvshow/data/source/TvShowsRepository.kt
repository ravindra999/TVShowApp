package com.themoviedb.weektvshow.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.source.local.TvShowsLocalDataSource
import com.themoviedb.weektvshow.data.source.remote.TvShowsRemoteDataSource
import com.themoviedb.weektvshow.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface TvShowsRepository {
    fun getTrendingTvShows(): Flow<PagingData<TvShow>>
    fun getSearchQueryTVShows(tvSearch:String): Flow<PagingData<TvShow>>

    //local data management
    suspend fun insertTvShows(tvShow: TvShow)
    fun getCachedTvShows(): Flow<List<TvShow>?>
    suspend fun clearCachedTvShows()
}

class TvShowsRepositoryImp @Inject constructor(
    private val tvShowsRemoteDataSource: TvShowsRemoteDataSource,
    private val tvShowsLocalDataSource: TvShowsLocalDataSource
) : TvShowsRepository {

    override fun getTrendingTvShows() = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvShowsPagingSource(Constants.TRENDING_PAGE_SOURCE ,tvShowsRemoteDataSource) }
    ).flow.flowOn(Dispatchers.Default)

    //remote
    override fun getSearchQueryTVShows(searchQuery: String)= Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvShowsPagingSource(Constants.SEARCH_PAGE_SOURCE,tvShowsRemoteDataSource,searchQuery) }
    ).flow.flowOn(Dispatchers.Default)


    override suspend fun insertTvShows(tvShow: TvShow) {
        tvShowsLocalDataSource.insertTvShows(tvShow)
    }

    override fun getCachedTvShows(): Flow<List<TvShow>?> {
        return tvShowsLocalDataSource.getCachedTvShows()
    }

    override suspend fun clearCachedTvShows() {
        tvShowsLocalDataSource.clearCachedTvShows()
    }

}