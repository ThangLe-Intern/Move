package com.madison.move.data.source.remote

import com.madison.move.data.model.Video
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.remote.model.MoveResponse
import com.madison.move.data.source.remote.services.MoveApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoveRemoteDataSource private constructor(movieApi: MoveApi) : MoveDataSource {
    private val moveApi: MoveApi

    init {
        this.moveApi = movieApi
    }

    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        moveApi.getMovies()?.enqueue(object : Callback<MoveResponse?> {
            override fun onResponse(
                call: Call<MoveResponse?>,
                response: Response<MoveResponse?>
            ) {
                val movies: List<Video?>? =
                    if (response.body() != null) response.body()!!.videos else null
                if (movies != null && movies.isNotEmpty()) {
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

    override fun saveVideos(videos: List<Video?>?) {

    }


    companion object {
        private var instance: MoveRemoteDataSource? = null
        fun getInstance(movieApi: MoveApi): MoveRemoteDataSource? {
            if (instance == null) {
                instance = MoveRemoteDataSource(movieApi)
            }
            return instance
        }
    }
}