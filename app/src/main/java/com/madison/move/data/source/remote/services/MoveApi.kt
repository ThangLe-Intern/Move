package com.madison.move.data.source.remote.services

import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.data.source.remote.model.MoveResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoveApi {
    @GET("movies")
    fun getMovies(): Call<MoveResponse?>?

    @GET("videos-carousel")
    fun getCarousel(): Call<CarouselResponse>

    @GET("featured-categories")
    fun getCategory(): Call<CategoryResponse>

    @GET("videos-you-may-like")
    fun getVideoSuggestion(): Call<VideoSuggestionResponse>

    @GET("videos/{id}")
    fun getDetailVideoSuggestion(
        @Header("Authorization") authorization : String,
        @Path("id") videoId :Int
    ): Call<VideoDetailResponse>




    @POST("login")
    fun loginApi(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): Call<LoginResponse>

}