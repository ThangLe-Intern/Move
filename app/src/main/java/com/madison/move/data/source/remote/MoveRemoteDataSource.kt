package com.madison.move.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.madison.move.data.model.Video
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.remote.model.MoveResponse
import com.madison.move.data.source.remote.services.MoveApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoveRemoteDataSource private constructor(private val moveApi: MoveApi) : MoveDataSource {
    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        moveApi.getMovies()?.enqueue(object : Callback<MoveResponse?> {
            override fun onResponse(
                call: Call<MoveResponse?>,
                response: Response<MoveResponse?>
            ) {
                val movies: List<Video?>? =
                    if (response.body() != null) response.body()!!.videos else null
                if (movies != null && movies.isNotEmpty()) {
                    callback?.onVideosLoaded(movies)
                } else {
                    callback?.onDataNotAvailable()
                }
            }

            override fun onFailure(call: Call<MoveResponse?>, t: Throwable) {
                callback?.onError()
            }
        })
    }


    override fun getCarousel(): Call<CarouselResponse> {
        return moveApi.getCarousel()
    }

    override fun setCarousel(): MutableLiveData<CarouselResponse> {
        return null!!
    }

    override fun getCategory(): Call<CategoryResponse>? {
        return moveApi.getCategory()
    }

    override fun getVideoSuggestion(): Call<VideoSuggestionResponse>? {
        return moveApi.getVideoSuggestion()
    }

    override fun getVideoDetail(authorization: String, id: Int): Call<VideoDetailResponse>? {
        return moveApi.getDetailVideoSuggestion(authorization, id)
    }

    override fun getTokenLogin(email: String, password: String): Call<LoginResponse>? {
        return moveApi.loginApi(email, password)
    }

    override fun saveVideos(videos: List<Video?>?) {

    }

    override fun testFun() = 2


    companion object {
        private var instance: MoveRemoteDataSource? = null
        fun getInstance(movieApi: MoveApi): MoveRemoteDataSource {
            if (instance == null) {
                instance = MoveRemoteDataSource(movieApi)
            }
            return instance!!
        }
    }
}