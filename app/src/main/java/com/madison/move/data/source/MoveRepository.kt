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

    override fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>>? {
        return moveRemote.getCarousel()
    }

    override fun getCategory(): Call<ObjectResponse<List<DataCategory>>>? {
        return moveRemote.getCategory()
    }

    override fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>>? {
        return moveRemote.getVideoSuggestion()
    }

    override fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>>? {
        return moveRemote.getVideoSuggestionForUser(token)
    }

    override fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>>? {
    override fun getVideoDetail(authorization:String,id: Int): Call<VideoDetailResponse>? {
        return moveRemote.getVideoDetail(authorization,id)
    }

    override fun getTokenLogin(email: String, password: String): Call<LoginResponse>? {
        return moveRemote.getTokenLogin(email, password)
    }

    override fun logOutUser(token: String): Call<ObjectResponse<DataUser>>? {
        return moveRemote.logOutUser(token)
    }

    override fun getUserProfile(token: String): Call<ObjectResponse<DataUser>>? {
        return moveRemote.getUserProfile(token)
    }

    override fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>? {
        return moveRemote.getCountryData()
    }

    override fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>>? {
        return moveRemote.getStateData(countryID)
    }

    override fun updateProfileUser(
        token: String,
        profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>? {
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