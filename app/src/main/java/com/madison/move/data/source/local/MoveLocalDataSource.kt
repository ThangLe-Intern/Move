package com.madison.move.data.source.local

import androidx.lifecycle.MutableLiveData
import com.madison.move.data.model.Video
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
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

    override fun getCarousel(): Call<CarouselResponse>? {
        return null
    }

    override fun setCarousel(): MutableLiveData<CarouselResponse> {
        return null!!
    }

    override fun getCategory(): Call<CategoryResponse>? {
        return null
    }

    override fun getVideoSuggestion(): Call<VideoSuggestionResponse>? {
        return null
    }

    override fun getVideoSuggestionForUser(token: String): Call<VideoSuggestionResponse>? {
        return null
    }

    override fun getTokenLogin(email: String, password: String): Call<LoginResponse>? {
        return null
    }


    override fun saveVideos(movies: List<Video?>?) {
        val runnable = Runnable { moveDao.saveMovies(movies) }
        executor.execute(runnable)
    }

    override fun testFun(): Int = 1
}