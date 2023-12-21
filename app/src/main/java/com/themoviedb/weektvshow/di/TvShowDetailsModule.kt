package com.themoviedb.weektvshow.di

import com.themoviedb.weektvshow.data.source.TvShowDetailsRepository
import com.themoviedb.weektvshow.data.source.TvShowDetailsRepositoryImp
import com.themoviedb.weektvshow.data.source.local.TvShowDetailsLocalDataSource
import com.themoviedb.weektvshow.data.source.local.TvShowDetailsLocalDataSourceImp
import com.themoviedb.weektvshow.data.source.remote.TvShowDetailsRemoteDataSource
import com.themoviedb.weektvshow.data.source.remote.TvShowDetailsRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TvShowDetailsModule {

    @Binds
    @Singleton
    abstract fun provideTvShowDetailsRemoteDataSource(
        tvShowDetailsRemoteDataSourceImp: TvShowDetailsRemoteDataSourceImp
    ): TvShowDetailsRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideTvShowDetailsLocalDataSource(
        tvShowDetailsLocalDataSourceImp: TvShowDetailsLocalDataSourceImp
    ): TvShowDetailsLocalDataSource

    @Binds
    @Singleton
    abstract fun provideTvShowDetailsRepository(
        tvShowDetailsRepositoryImp: TvShowDetailsRepositoryImp
    ): TvShowDetailsRepository
}