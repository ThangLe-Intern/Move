package com.madison.move.data.source.remote.services

import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.ProfileResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.data.source.remote.model.MoveResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
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

    @GET("videos/{id}")
    fun getDetailVideoSuggestion(
        @Path("id") videoId :Int
    ): Call<VideoDetailResponse>




    @GET("videos-you-may-like")
    fun getVideoSuggestionForUser(@Header("Authorization") authorization: String): Call<VideoSuggestionResponse>


    @POST("login")
    fun loginApi(
        @Query("email")
        email: String,
        @Query("password")
        password: String
    ): Call<LoginResponse>

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
