package com.madison.move.data.source

import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.comment.SendComment
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
    fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>>?
    fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>>?
    fun logOutUser(token: String): Call<ObjectResponse<DataUser>>?
    fun getUserProfile(token: String): Call<ObjectResponse<DataUser>>?
    fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>?
    fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>>?
    fun updateProfileUser(
        token: String, profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>?

    fun getVideoDetail(id: Int): Call<ObjectResponse<DataVideoDetail>>?
    fun getCommentVideo(token: String, id: Int): Call<ObjectResponse<List<DataComment>>>?

    fun sendComment(
        token: String, videoId: Int, content: SendComment
    ): Call<ObjectResponse<CommentResponse>>?

    fun sendReply(token: String, commentId: Int, content: SendComment):
            Call<ObjectResponse<CommentResponse>>?

    fun callLikeComment(token: String, commentId: Int): Call<LikeResponse>?

    fun callDiskLikeComment(token: String, commentId: Int): Call<DiskLikeResponse>?

    fun getFaq(): Call<ObjectResponse<List<DataFAQ>>>?
    fun getGuidelines(): Call<ObjectResponse<List<DataGuidelines>>>?

    fun postView(
        token: String,
        videoId: Int,
        time: PostView
    ): Call<ObjectResponse<PostViewResponse>>?

}