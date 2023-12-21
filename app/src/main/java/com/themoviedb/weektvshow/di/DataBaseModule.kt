package com.themoviedb.weektvshow.di

import android.content.Context
import androidx.room.Room
import com.themoviedb.weektvshow.data.room.TvShowDataBase
import com.themoviedb.weektvshow.data.room.daos.FavsTvShowsDao
import com.themoviedb.weektvshow.data.room.daos.TvShowListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideTvShowDao(tvShowDataBase: TvShowDataBase): TvShowListDao {
        return tvShowDataBase.tvShowListDao()
    }

    @Singleton
    @Provides
    fun provideFavsTvShowsDao(tvShowDataBase: TvShowDataBase): FavsTvShowsDao {
        return tvShowDataBase.favsTvShowsDao()
    }

    @Singleton
    @Provides
    fun provideTvShowAppDataBase(@ApplicationContext appContext: Context): TvShowDataBase {
        return Room.databaseBuilder(
            appContext,
            TvShowDataBase::class.java,
            "TvShow_DB"
        ).fallbackToDestructiveMigration().build()
    }
}