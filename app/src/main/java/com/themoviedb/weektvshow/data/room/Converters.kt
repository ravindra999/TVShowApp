package com.themoviedb.weektvshow.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    @TypeConverter
    fun stringListToString(tvShows: List<String>): String? {
        return gson.toJson(tvShows)
    }

    @TypeConverter
    fun stringToStringList(json: String): List<String>? {
        if (json.isEmpty()) return emptyList()

        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun intListToString(tvShows: List<Int>): String? {
        return gson.toJson(tvShows)
    }

    @TypeConverter
    fun stringToIntList(json: String): List<Int>? {
        if (json.isEmpty()) return emptyList()

        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(json, listType)
    }
}