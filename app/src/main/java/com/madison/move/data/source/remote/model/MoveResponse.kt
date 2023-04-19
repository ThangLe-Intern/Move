package com.madison.move.data.source.remote.model

import com.aliasadi.androidmvp.data.movie.Movie

/**
 * Created by Ali Asadi on 24/03/2018.
 */
class MovieResponse {
    @Expose
    @SerializedName("movies")
    var movies: List<Movie>? = null
}