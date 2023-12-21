package com.themoviedb.weektvshow.data.network.models.tvshows

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.themoviedb.weektvshow.utils.Exclude

@Entity
data class TvShow(
    val backdrop_path: String? = null,
    val first_air_date: String,
    val genre_ids: List<Int>,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int,
    @Exclude
    val timestamp: Long = System.currentTimeMillis()
)