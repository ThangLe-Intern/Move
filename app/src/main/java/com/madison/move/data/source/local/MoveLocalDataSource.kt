package com.madison.move.data.source.local

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
import com.madison.move.utils.DiskExecutor
import retrofit2.Call
import java.util.concurrent.Executor


class MoveLocalDataSource private constructor(
    private val executor: Executor,
    private val moveDao: MoveDao
) :
    MoveDataSource {


    companion object {
        private var instance: MoveLocalDataSource? = null
        fun getInstance(movieDao: MoveDao): MoveLocalDataSource {
            if (instance == null) {
                instance = MoveLocalDataSource(DiskExecutor(), movieDao)
            }
            return instance!!
        }
    }

    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        val runnable = Runnable {
            val videos: List<Video?>? = moveDao.videos
            if (videos != null && videos.isNotEmpty()) {
                callback?.onVideosLoaded(videos)
            } else {
                callback?.onDataNotAvailable()
            }
        }
        executor.execute(runnable)
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

    override fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>>? {
        return null
    }

    override fun getVideoDetail(authorization: String, id: Int): Call<VideoDetailResponse>? {
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


    override fun saveVideos(movies: List<Video?>?) {
        val runnable = Runnable { moveDao.saveMovies(movies) }
        executor.execute(runnable)
    }

}