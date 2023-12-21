package com.themoviedb.weektvshow.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.themoviedb.weektvshow.data.network.models.tvshows.TvShow
import com.themoviedb.weektvshow.data.room.daos.TvShowListDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class TvShowListDaoTest {

    private lateinit var dataBase: TvShowDataBase
    private lateinit var dao: TvShowListDao

    @Before
    fun setup() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TvShowDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = dataBase.tvShowListDao()
    }

    @After
    fun teardown() {
        dataBase.close()
    }

    @Test
    fun testInsertAndGetTvShowList() = runTest {

        for (i in 0..5) {
            val tvShow = TvShow(
                backdrop_path = null,
                first_air_date = "20-01-2022",
                genre_ids = emptyList(),
                id = 5050,
                name = "Cars $i",
                origin_country = emptyList(),
                original_language = "en",
                original_name = "",
                overview = "",
                popularity = 4000.5,
                poster_path = "",
                vote_average = 8.5,
                vote_count = 5000,
                timestamp = System.currentTimeMillis()
            )

            dao.insertTvShow(tvShow)
        }

        val tvShows = dao.getCachedTvShows()

        assertThat(tvShows?.get(0)?.name).contains("Cars 1")
    }

    @Test
    fun testClearTvShows() = runTest {
        val tvShow = TvShow(
            backdrop_path = null,
            first_air_date = "20-01-2022",
            genre_ids = emptyList(),
            id = 5050,
            name = "Cars",
            origin_country = emptyList(),
            original_language = "en",
            original_name = "",
            overview = "",
            popularity = 4000.5,
            poster_path = "",
            vote_average = 8.5,
            vote_count = 5000,
            timestamp = System.currentTimeMillis()
        )

        dao.insertTvShow(tvShow)

        dao.clearTvShows()

        val tvShows = dao.getCachedTvShows()

        assertThat(tvShows).isEmpty()
    }

    @Test
    fun testLastDateUpdatedInsertIsCorrect() = runTest {
        val tvShow = TvShow(
            backdrop_path = null,
            first_air_date = "20-01-2022",
            genre_ids = emptyList(),
            id = 5050,
            name = "Cars",
            origin_country = emptyList(),
            original_language = "en",
            original_name = "",
            overview = "",
            popularity = 4000.5,
            poster_path = "",
            vote_average = 8.5,
            vote_count = 5000,
            timestamp = System.currentTimeMillis()
        )

        dao.insertTvShow(tvShow)

        val currentMillis = System.currentTimeMillis()
        val insertedDateMillis = dao.getCachedTvShows()?.get(0)?.timestamp

        assertThat(currentMillis).isNotEqualTo(insertedDateMillis)
    }

    @Test
    fun testIdIsEqual() = runTest {
        val tvShow = TvShow(
            backdrop_path = null,
            first_air_date = "20-01-2022",
            genre_ids = emptyList(),
            id = 5050,
            name = "Cars",
            origin_country = emptyList(),
            original_language = "en",
            original_name = "",
            overview = "",
            popularity = 4000.5,
            poster_path = "",
            vote_average = 8.5,
            vote_count = 5000,
            timestamp = System.currentTimeMillis()
        )

        dao.insertTvShow(tvShow)

        val tvShows = dao.getCachedTvShows()
        assertThat(tvShows?.get(0)?.id).isEqualTo(5050)
    }
}