package com.themoviedb.weektvshow.utils

import com.themoviedb.weektvshow.BuildConfig

fun String.getImageByPath(): String {
    val baseUrl =     BuildConfig.TVSHOW_IMAGE_BASE_URL
    return "$baseUrl${this}"
}