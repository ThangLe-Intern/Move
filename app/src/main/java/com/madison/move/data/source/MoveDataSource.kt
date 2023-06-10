package com.madison.move.data.source

import com.madison.move.data.model.*
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import retrofit2.Call


interface MoveDataSource {
    interface LoadVideosCallback {
        fun onVideosLoaded(videos: List<Video?>?)
        fun onDataNotAvailable()
        fun onError()
    }

    fun getVideos(callback: LoadVideosCallback?)
    fun saveVideos(videos: List<Video?>?)
    fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>>?
    fun getCategory(): Call<ObjectResponse<List<DataCategory>>>?
    fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>>?
    fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>>?
    fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>>?
    fun logOutUser(token: String): Call<ObjectResponse<DataUser>>?
    fun getUserProfile(token: String): Call<ObjectResponse<DataUser>>?
    fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>?
    fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>>?
    fun updateProfileUser(
        token: String, profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>?

    fun getVideoDetail(id: Int): Call<ObjectResponse<DataVideoDetail>>?
    fun getCommentVideo(token:String, id: Int): Call<ObjectResponse<List<DataComment>>>?



}