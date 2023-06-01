package com.madison.move.data.source.remote.services

import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.logout.LogoutResponse
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.ProfileResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.data.source.remote.model.MoveResponse
import retrofit2.Call
import retrofit2.http.*

interface MoveApi {
    @GET("movies")
    fun getMovies(): Call<MoveResponse?>?

    @GET("videos-carousel")
    fun getCarousel(): Call<CarouselResponse>

    @GET("featured-categories")
    fun getCategory(): Call<CategoryResponse>

    @GET("videos-you-may-like")
    fun getVideoSuggestion(): Call<VideoSuggestionResponse>

    @GET("videos-you-may-like")
    fun getVideoSuggestionForUser(@Header("Authorization") authorization: String): Call<VideoSuggestionResponse>


    @POST("login")
    fun loginApi(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): Call<LoginResponse>

    @POST("logout")
    fun logoutRequest(@Header("Authorization") authorization: String):Call<LogoutResponse>

    @GET("users/information")
    fun getUserProfile(@Header("Authorization") authorization: String): Call<ProfileResponse>

    @GET("countries")
    fun getCountry(): Call<CountryResponse>

    @GET("countries/{id}/states")
    fun getStates(@Path("id") countryId: Int): Call<StateResponse>

    @PUT("users/update-profile")
    fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body requestBody: ProfileRequest
    ): Call<UpdateProfileResponse>
}
