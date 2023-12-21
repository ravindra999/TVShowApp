package com.themoviedb.weektvshow.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTvShow(
    val first_air_date: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val vote_average: Double
)
