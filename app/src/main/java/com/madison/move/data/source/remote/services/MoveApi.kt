package com.madison.move.data.source.remote.services

import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.comment.SendComment
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
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
    fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>>

    @GET("featured-categories")
    fun getCategory(): Call<ObjectResponse<List<DataCategory>>>

    @GET("videos-you-may-like")
    fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>>

    @GET("videos/{id}")
    fun getDetailVideoSuggestion(
        @Path("id") videoId: Int
    ): Call<VideoDetailResponse>


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
    fun logoutRequest(@Header("Authorization") authorization: String): Call<ObjectResponse<DataUser>>

    @GET("users/information")
    fun getUserProfile(@Header("Authorization") authorization: String): Call<ObjectResponse<DataUser>>

    @GET("countries")
    fun getCountry(): Call<ObjectResponse<List<DataCountry>>>

    @GET("countries/{id}/states")
    fun getStates(@Path("id") countryId: Int): Call<ObjectResponse<List<DataState>>>
    @GET("faqs")
    fun getFaq(): Call<ObjectResponse<List<DataFAQ>>>

    @GET("community-guidelines")
    fun getGuidelines(): Call<ObjectResponse<List<DataGuidelines>>>

    @PUT("users/update-profile")
    fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body requestBody: ProfileRequest
    ): Call<ObjectResponse<DataUser>>

    @GET("showVideos/{id}")
    fun showVideoDetail(@Path("id") videoId: Int): Call<ObjectResponse<DataVideoDetail>>

    @GET("comments/{id}")
    fun getCommentVideo(
        @Header("Authorization") authorization: String,
        @Path("id") videoId: Int
    ): Call<ObjectResponse<List<DataComment>>>

    @POST("videos/{id}/comments")
    fun sendComment(
        @Header("Authorization") authorization: String,
        @Path("id") videoId: Int,
        @Body
        content: SendComment
    ): Call<ObjectResponse<CommentResponse>>

    @POST("comments/{id}/reply")
    fun sendReply(
        @Header("Authorization") authorization: String,
        @Path("id") commentId: Int,
        @Body
        content: SendComment
    ): Call<ObjectResponse<CommentResponse>>
}
