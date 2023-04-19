package com.madison.move.data.source.local

import com.aliasadi.androidmvp.data.movie.Movie
import com.madison.move.data.model.Video
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.local.dao.MoveDao
import java.util.concurrent.Executor

/**
 * Created by Ali Asadi on 30/01/2019.
 */
class MovieLocalDataSource private constructor(private val executor: Executor, movieDao: MoveDao) :
    MoveDataSource {
    private val movieDao: MoveDao

    init {
        this.movieDao = movieDao
    }

    fun getMovies(callback: MoveDataSource.LoadMoviesCallback) {
        val runnable = Runnable {
            val movies: List<Video> = movieDao.getMovies()
            if (!movies.isEmpty()) {
                callback.onVideosLoaded(movies)
            } else {
                callback.onDataNotAvailable()
            }
        }
        executor.execute(runnable)
    }

    fun saveMovies(movies: List<Video?>?) {
        val runnable = Runnable { movieDao.saveMovies(movies) }
        executor.execute(runnable)
    }

    companion object {
        private var instance: MovieLocalDataSource? = null
        fun getInstance(movieDao: MoveDao): MovieLocalDataSource? {
            if (instance == null) {
                instance = MovieLocalDataSource(DiskExecutor(), movieDao)
            }
            return instance
        }
    }
}