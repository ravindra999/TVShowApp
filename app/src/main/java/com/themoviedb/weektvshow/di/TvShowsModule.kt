package com.themoviedb.weektvshow.di

import com.themoviedb.weektvshow.data.source.TvShowsRepository
import com.themoviedb.weektvshow.data.source.TvShowsRepositoryImp
import com.themoviedb.weektvshow.data.source.local.TvShowsLocalDataSource
import com.themoviedb.weektvshow.data.source.local.TvShowsLocalDataSourceImp
import com.themoviedb.weektvshow.data.source.remote.TvShowsRemoteDataSource
import com.themoviedb.weektvshow.data.source.remote.TvShowsRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TvShowsModule {

    @Binds
    @Singleton
    abstract fun provideTvShowsRemoteDataSource(
        tvShowsRemoteDataSourceImp: TvShowsRemoteDataSourceImp
    ): TvShowsRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideTvShowsLocalDataSource(
        tvShowsLocalDataSourceImp: TvShowsLocalDataSourceImp
    ): TvShowsLocalDataSource

    @Binds
    @Singleton
    abstract fun provideTvShowRepository(
        tvShowsRepositoryImp: TvShowsRepositoryImp
    ): TvShowsRepository
}