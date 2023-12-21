package com.themoviedb.weektvshow.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.room.daos.FavsTvShowsDao
import com.themoviedb.weektvshow.data.room.daos.TvShowListDao
import com.themoviedb.weektvshow.data.room.entities.FavoriteTvShow

@Database(
    entities = [TvShow::class, FavoriteTvShow::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TvShowDataBase : RoomDatabase() {
    abstract fun favsTvShowsDao(): FavsTvShowsDao
    abstract fun tvShowListDao(): TvShowListDao
}