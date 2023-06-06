package com.madison.move.data.source.remote.services

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.DataCountry
import com.madison.move.data.model.DataState
import com.madison.move.data.model.ProfileRequest
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.data.source.remote.model.MoveResponse
import retrofit2.Call
import retrofit2.http.*

interface MoveApi {
    @GET("movies")
    fun getMovies(): Call<MoveResponse?>?

    @GET("videos-carousel")
    fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>>

    @GET("featured-categories")
    fun getCategory(): Call<ObjectResponse<List<DataCategory>>>

    @GET("videos-you-may-like")
    fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>>

    @GET("videos-you-may-like")
    fun getVideoSuggestionForUser(@Header("Authorization") authorization: String): Call<ObjectResponse<VideoSuggestion>>


    @POST("login")
    fun loginApi(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): Call<ObjectResponse<DataUser>>

    @POST("logout")
    fun logoutRequest(@Header("Authorization") authorization: String):Call<ObjectResponse<DataUser>>

    @GET("users/information")
    fun getUserProfile(@Header("Authorization") authorization: String): Call<ObjectResponse<DataUser>>

    @GET("countries")
    fun getCountry(): Call<ObjectResponse<List<DataCountry>>>

    @GET("countries/{id}/states")
    fun getStates(@Path("id") countryId: Int): Call<ObjectResponse<List<DataState>>>

    @PUT("users/update-profile")
    fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body requestBody: ProfileRequest
    ): Call<ObjectResponse<DataUser>>
}
