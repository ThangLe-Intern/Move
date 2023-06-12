package com.madison.move.data.source.local

import android.util.SparseArray
import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.comment.SendComment
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

    override fun getVideoDetail(id: Int): Call<ObjectResponse<DataVideoDetail>>? {
        return null
    }

    override fun getCommentVideo(token: String,id: Int): Call<ObjectResponse<Map<String, DataComment?>>>? {
        return null
    }

    override fun sendComment(token: String, videoId: Int, content: SendComment): Call<ObjectResponse<CommentResponse>>? {
        return null
    }

    override fun sendReply(token: String, commentId: Int, content: SendComment): Call<ObjectResponse<CommentResponse>>? {
        return null
    }

    override fun getFaq(): Call<ObjectResponse<List<DataFAQ>>>? {
        return null
    }

    override fun getGuidelines(): Call<ObjectResponse<List<DataGuidelines>>>? {
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
        token: String, profileRequest: ProfileRequest
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