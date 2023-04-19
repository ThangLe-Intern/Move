package com.madison.move.data.source.local

import android.util.SparseArray

/**
 * Created by Ali Asadi on 30/01/2019.
 */
class MovieCacheDataSource : MovieDataSource {
    private val cachedMovies: SparseArray<Movie> = SparseArray<Movie>()
    fun getMovies(callback: LoadMoviesCallback) {
        if (cachedMovies.size() > 0) {
            val movies: MutableList<Movie> = ArrayList<Movie>()
            for (i in 0 until cachedMovies.size()) {
                val key = cachedMovies.keyAt(i)
                movies.add(cachedMovies[key])
            }
            callback.onMoviesLoaded(movies)
        } else {
            callback.onDataNotAvailable()
        }
    }

    fun saveMovies(movies: List<Movie?>) {
        cachedMovies.clear()
        for (movie in movies) {
            cachedMovies.put(movie.getId(), movie)
        }
    }

    companion object {
        private var sInstance: MovieCacheDataSource? = null
        fun getsInstance(): MovieCacheDataSource? {
            if (sInstance == null) {
                sInstance = MovieCacheDataSource()
            }
            return sInstance
        }
    }
}