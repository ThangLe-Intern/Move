package com.madison.move.data.source.remote.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoveService {
    private val URL = "https://demo7812962.mockable.io/"

    private var mMovieApi: MoveApi? = null

    private var singleton: MoveService? =
        null

    private fun MovieService() {
        val mRetrofit =
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(URL)
                .build()
        mMovieApi = mRetrofit.create(MoveApi::class.java)
    }

    fun getInstance(): MoveService? {
        if (singleton == null) {
            singleton = MoveService()
        }
        return singleton
    }

    fun getMovieApi(): MoveApi? {
        return mMovieApi
    }
}