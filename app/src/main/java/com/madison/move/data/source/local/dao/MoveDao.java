package com.madison.move.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.madison.move.data.model.Video;

import java.util.List;

@Dao
public interface MovieDao {

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM videos")
    List<Video> getMovies();

    /**
     * Insert all movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovies(List<Video> movies);

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM videos")
    void deleteMovies();
}
