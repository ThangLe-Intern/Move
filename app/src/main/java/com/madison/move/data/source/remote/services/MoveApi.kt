package com.madison.move.data.source.remote.services

import com.madison.move.data.source.remote.model.MoveResponse
import retrofit2.Call
import retrofit2.http.GET

interface MoveApi {
    @GET("/movies")
    fun getMovies(): Call<MoveResponse?>?
}