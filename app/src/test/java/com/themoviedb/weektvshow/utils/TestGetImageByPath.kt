package com.themoviedb.weektvshow.utils

import com.themoviedb.weektvshow.BuildConfig
import org.junit.Assert
import org.junit.Test

class TestGetImageByPath {

    @Test
    fun `test if string return a path with image base url`() {
        val path = "image.jpg"
        val finalPath = path.getImageByPath()

        Assert.assertEquals(
            "${BuildConfig.TVSHOW_IMAGE_BASE_URL}$path",
            finalPath
        )
    }
}