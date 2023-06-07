package com.madison.move.data.source

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.Video
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.DataCountry
import com.madison.move.data.model.DataState
import com.madison.move.data.model.ProfileRequest
import com.madison.move.data.model.DataUser
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
    fun getVideoSuggestionForUser(token:String): Call<ObjectResponse<VideoSuggestion>>?
    fun getTokenLogin(email:String, password:String): Call<ObjectResponse<DataUser>>?
    fun logOutUser(token: String): Call<ObjectResponse<DataUser>>?
    fun getUserProfile(token: String): Call<ObjectResponse<DataUser>>?
    fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>?
    fun getStateData(countryID: Int):Call<ObjectResponse<List<DataState>>>?
    fun updateProfileUser(token: String, profileRequest: ProfileRequest): Call<ObjectResponse<DataUser>>?
    fun getVideoDetail(authorization: String,id : Int):Call<VideoDetailResponse>?
}