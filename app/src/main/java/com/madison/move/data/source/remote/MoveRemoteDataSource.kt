package com.madison.move.data.source.remote

import com.madison.move.data.model.Video
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.remote.model.MoveResponse
import com.madison.move.data.source.remote.services.MoveApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRemoteDataSource private constructor(movieApi: MoveApi) : MoveDataSource {
    private val movieApi: MoveApi

    init {
        this.movieApi = movieApi
    }

    override fun getVideos(callback: MoveDataSource.LoadMoviesCallback?) {
        movieApi.getMovies()?.enqueue(object : Callback<MoveResponse?> {
            override fun onResponse(
                call: Call<MoveResponse?>,
                response: Response<MoveResponse?>
            ) {
                val movies: List<Video?>? =
                    if (response.body() != null) response.body()!!.getVideos() else null
                if (movies != null && !movies.isEmpty()) {
                    callback?.onVideosLoaded(movies)
                } else {
                    callback?.onDataNotAvailable()
                }
            }

            override fun onFailure(call: Call<MoveResponse?>, t: Throwable) {
                callback?.onError()
            }
        })
    }

    override fun saveVideos(movies: List<Video?>?) {

    }


    companion object {
        private var instance: MovieRemoteDataSource? = null
        fun getInstance(movieApi: MoveApi): MovieRemoteDataSource? {
            if (instance == null) {
                instance = MovieRemoteDataSource(movieApi)
            }
            return instance
        }
    }
}