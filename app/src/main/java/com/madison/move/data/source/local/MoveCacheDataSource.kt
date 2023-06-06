package com.madison.move.data.source.local

import android.util.SparseArray
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

    override fun getVideoDetail(authorization: String, id: Int): Call<VideoDetailResponse>? {
        return null
    }

    override fun getVideoSuggestionForUser(token: String): Call<VideoSuggestionResponse>? {
        return null
    }


    override fun getTokenLogin(email: String, password: String): Call<LoginResponse>? {
        return null
    }

    override fun getUserProfile(token: String): Call<ProfileResponse>? {
        return null
    }

    override fun getCountryData(): Call<CountryResponse>? {
        return null
    }

    override fun getStateData(countryID: Int): Call<StateResponse>? {
        return null
    }

    override fun updateProfileUser(
        token: String,
        profileRequest: ProfileRequest
    ): Call<UpdateProfileResponse>? {
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

    override fun testFun(): Int = 0
}