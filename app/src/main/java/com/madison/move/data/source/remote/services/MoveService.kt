package com.madison.move.data.source.remote.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoveService {

    var mMovieApi: MoveApi

    companion object {
        const val URL: String = "https://api.move-intern-stg.madlab.tech/api/"
        private var singleton: MoveService? = null
        fun getInstance(): MoveService {
            if (singleton == null) {
                singleton = MoveService()
            }
            return singleton!!
        }
    }

    init {
        val mRetrofit =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(URL)
                .build()
        mMovieApi = mRetrofit.create(MoveApi::class.java)
    }

    fun getMovieApi(): MoveApi {
        return mMovieApi
    }
}