package com.madison.move.data.source

import androidx.lifecycle.MutableLiveData
import com.madison.move.data.model.Video
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
import retrofit2.Call


interface MoveDataSource {
    interface LoadVideosCallback {
        fun onVideosLoaded(videos: List<Video?>?)
        fun onDataNotAvailable()
        fun onError()
    }

    fun getVideos(callback: LoadVideosCallback?)
    fun saveVideos(videos: List<Video?>?)
    fun testFun(): Int
    fun getCarousel(): Call<CarouselResponse>?
    fun setCarousel(): MutableLiveData<CarouselResponse>
    fun getCategory(): Call<CategoryResponse>?
    fun getVideoSuggestion(): Call<VideoSuggestionResponse>?
    fun getVideoSuggestionForUser(token:String): Call<VideoSuggestionResponse>?
    fun getTokenLogin(email:String, password:String): Call<LoginResponse>?
    fun logOutUser(token: String): Call<LogoutResponse>?
    fun getUserProfile(token: String): Call<ProfileResponse>?
    fun getCountryData(): Call<CountryResponse>?
    fun getStateData(countryID: Int):Call<StateResponse>?
    fun updateProfileUser(token: String, profileRequest: ProfileRequest): Call<UpdateProfileResponse>?

}