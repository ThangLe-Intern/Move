package com.madison.move.data.source.local

import android.util.SparseArray
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.Video
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.DataCountry
import com.madison.move.data.model.DataState
import com.madison.move.data.model.ProfileRequest
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.data.source.MoveDataSource
import retrofit2.Call


class MoveCacheDataSource : MoveDataSource {


    private val cachedMovies: SparseArray<Video> = SparseArray<Video>()

    companion object {
        private var sInstance: MoveCacheDataSource? = null
        fun getsInstance(): MoveCacheDataSource {
            if (sInstance == null) {
                sInstance = MoveCacheDataSource()
            }
            return sInstance!!
        }
    }

    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        if (cachedMovies.size() > 0) {
            val videos: MutableList<Video> = ArrayList<Video>()
            for (i in 0 until cachedMovies.size()) {
                val key = cachedMovies.keyAt(i)
                videos.add(cachedMovies[key])
            }
            callback?.onVideosLoaded(videos)
        } else {
            callback?.onDataNotAvailable()
        }
    }

    override fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>>? {
        return null
    }

    override fun getCategory(): Call<ObjectResponse<List<DataCategory>>>? {
        return null
    }

    override fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>>? {
        return null
    }

    override fun getVideoDetail(authorization: String, id: Int): Call<VideoDetailResponse>? {
        return null
    }

    override fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>>? {
        return null
    }


    override fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>>? {
        return null
    }

    override fun logOutUser(token: String): Call<ObjectResponse<DataUser>>? {
        return null
    }

    override fun getUserProfile(token: String): Call<ObjectResponse<DataUser>>? {
        return null
    }

    override fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>? {
        return null
    }

    override fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>>? {
        return null
    }

    override fun updateProfileUser(
        token: String,
        profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>? {
        return null
    }


    override fun saveVideos(videos: List<Video?>?) {
        cachedMovies.clear()
        if (videos != null) {
            for (video in videos) {
                video?.let { it.id?.let { videos -> cachedMovies.put(videos, video) } }
            }
        }
    }

}