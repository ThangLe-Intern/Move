package com.madison.move.data.source

import androidx.lifecycle.MutableLiveData
import com.madison.move.data.model.Video
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.country.CountryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.state.StateResponse
import com.madison.move.data.model.update_profile.ProfileRequest
import com.madison.move.data.model.update_profile.UpdateProfileResponse
import com.madison.move.data.model.user_profile.ProfileResponse
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.data.source.local.MoveCacheDataSource
import com.madison.move.data.source.local.MoveLocalDataSource
import com.madison.move.data.source.remote.MoveRemoteDataSource
import retrofit2.Call


class MoveRepository private constructor(
    private val moveRemote: MoveDataSource,
    private val movieLocal: MoveDataSource,
    private val movieCache: MoveDataSource,
) : MoveDataSource {
    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        if (callback == null) return
        movieCache.getVideos(object : MoveDataSource.LoadVideosCallback {

            override fun onVideosLoaded(videos: List<Video?>?) {
                callback.onVideosLoaded(videos)
            }

            override fun onDataNotAvailable() {
            }

            override fun onError() {
            }

        })
    }

    override fun saveVideos(videos: List<Video?>?) {
        movieLocal.saveVideos(videos)
    }

    override fun testFun() = moveRemote.testFun()
    override fun getCarousel(): Call<CarouselResponse>? {
        return moveRemote.getCarousel()
    }

    override fun setCarousel(): MutableLiveData<CarouselResponse> {
        getCarousel()
        return moveRemote.setCarousel()
    }

    override fun getCategory(): Call<CategoryResponse>? {
        return moveRemote.getCategory()
    }

    override fun getVideoSuggestion(): Call<VideoSuggestionResponse>? {
        return moveRemote.getVideoSuggestion()
    }

    override fun getVideoSuggestionForUser(token: String): Call<VideoSuggestionResponse>? {
        return moveRemote.getVideoSuggestionForUser(token)
    }

    override fun getVideoDetail(authorization:String,id: Int): Call<VideoDetailResponse>? {
        return moveRemote.getVideoDetail(authorization,id)
    }

    override fun getTokenLogin(email: String, password: String): Call<LoginResponse>? {
        return moveRemote.getTokenLogin(email, password)
    }

    override fun getUserProfile(token: String): Call<ProfileResponse>? {
        return moveRemote.getUserProfile(token)
    }

    override fun getCountryData(): Call<CountryResponse>? {
        return moveRemote.getCountryData()
    }

    override fun getStateData(countryID: Int): Call<StateResponse>? {
        return moveRemote.getStateData(countryID)
    }

    override fun updateProfileUser(
        token: String,
        profileRequest: ProfileRequest
    ): Call<UpdateProfileResponse>? {
        return moveRemote.updateProfileUser(token,profileRequest)
    }


    private fun getMoviesFromLocalDataSource(callback: MoveDataSource.LoadVideosCallback) {
        movieLocal.getVideos(object : MoveDataSource.LoadVideosCallback {
            override fun onVideosLoaded(videos: List<Video?>?) {
                callback.onVideosLoaded(videos)
                refreshCache(videos)
            }

            override fun onDataNotAvailable() {
                getVideosFromRemoteDataSource(callback)
            }

            override fun onError() {

            }
        })
    }

    private fun getVideosFromRemoteDataSource(callback: MoveDataSource.LoadVideosCallback) {
        moveRemote.getVideos(object : MoveDataSource.LoadVideosCallback {

            override fun onVideosLoaded(videos: List<Video?>?) {
                callback.onVideosLoaded(videos)
                saveVideos(videos)
                refreshCache(videos)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onError() {
                callback.onError()
            }
        })
    }

    private fun refreshCache(movies: List<Video?>?) {
        movieCache.saveVideos(movies)
    }

    fun destroyInstance() {
        instance = null
    }

    companion object {
        private var instance: MoveRepository? = null
        fun getInstance(
            movieRemote: MoveRemoteDataSource,
            movieLocal: MoveLocalDataSource,
            movieCache: MoveCacheDataSource
        ): MoveRepository {
            if (instance == null) {
                instance = MoveRepository(movieRemote, movieLocal, movieCache)
            }
            return instance!!
        }
    }
}