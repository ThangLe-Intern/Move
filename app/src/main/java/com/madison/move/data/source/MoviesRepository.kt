package com.madison.move.data.source.remote

import com.aliasadi.androidmvp.data.movie.Movie

/**
 * Created by Ali Asadi on 29/01/2019.
 */
class MoviesRepository private constructor(
    movieRemote: MovieRemoteDataSource,
    movieLocal: MovieLocalDataSource,
    movieCache: MovieCacheDataSource
) : MovieDataSource {
    private val movieRemote: MovieDataSource
    private val movieLocal: MovieDataSource
    private val movieCache: MovieDataSource

    init {
        this.movieRemote = movieRemote
        this.movieLocal = movieLocal
        this.movieCache = movieCache
    }

    fun getMovies(callback: LoadMoviesCallback?) {
        if (callback == null) return
        movieCache.getMovies(object : LoadMoviesCallback() {
            fun onMoviesLoaded(movies: List<Movie?>?) {
                callback.onMoviesLoaded(movies)
            }

            fun onDataNotAvailable() {
                getMoviesFromLocalDataSource(callback)
            }

            fun onError() {
                //not implemented in cache data source
            }
        })
    }

    fun saveMovies(movies: List<Movie>?) {
        movieLocal.saveMovies(movies)
    }

    private fun getMoviesFromLocalDataSource(callback: LoadMoviesCallback) {
        movieLocal.getMovies(object : LoadMoviesCallback() {
            fun onMoviesLoaded(movies: List<Movie>) {
                callback.onMoviesLoaded(movies)
                refreshCache(movies)
            }

            fun onDataNotAvailable() {
                getMoviesFromRemoteDataSource(callback)
            }

            fun onError() {
                //not implemented in local data source
            }
        })
    }

    private fun getMoviesFromRemoteDataSource(callback: LoadMoviesCallback) {
        movieRemote.getMovies(object : LoadMoviesCallback() {
            fun onMoviesLoaded(movies: List<Movie>) {
                callback.onMoviesLoaded(movies)
                saveMovies(movies)
                refreshCache(movies)
            }

            fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            fun onError() {
                callback.onError()
            }
        })
    }

    private fun refreshCache(movies: List<Movie>) {
        movieCache.saveMovies(movies)
    }

    fun destroyInstance() {
        instance = null
    }

    companion object {
        private var instance: MoviesRepository? = null
        fun getInstance(
            movieRemote: MovieRemoteDataSource,
            movieLocal: MovieLocalDataSource,
            movieCache: MovieCacheDataSource
        ): MoviesRepository? {
            if (instance == null) {
                instance = MoviesRepository(movieRemote, movieLocal, movieCache)
            }
            return instance
        }
    }
}