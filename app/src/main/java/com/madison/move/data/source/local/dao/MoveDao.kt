package com.madison.move.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madison.move.data.model.Video

@Dao
interface MoveDao {
    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @get:Query("SELECT * FROM videos")
    val videos: List<Video?>?

    /**
     * Insert all movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(movies: List<Video?>?)

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM videos")
    fun deleteMovies()
}