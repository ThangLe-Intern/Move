package com.madison.move.data.model.comment


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.madison.move.data.model.DataUser

data class CommentResponse(
    @SerializedName("content")
    @Expose
    val content: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("times")
    @Expose
    val times: String?,
    @SerializedName("user")
    @Expose
    val user: DataUser?,
    @SerializedName("video_id")
    @Expose
    val videoId: Int?
)