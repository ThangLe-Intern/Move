package com.madison.move.data.model.comment


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.madison.move.data.model.DataUser

data class DataComment(
    @SerializedName("comment_id")
    @Expose
    val commentId: Int?,
    @SerializedName("content")
    @Expose
    var content: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("times")
    @Expose
    val createdTime: String?,
    @SerializedName("dislike_count")
    @Expose
    val dislikeCount: Int?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("is_disliked")
    @Expose
    val isDisliked: Boolean?,
    @SerializedName("is_liked")
    @Expose
    val isLiked: Boolean?,
    @SerializedName("like_count")
    @Expose
    val likeCount: Int?,
    @SerializedName("replies")
    @Expose
    val replies: List<DataComment>?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?,
    @SerializedName("user")
    @Expose
    val user: DataUser?,
    @SerializedName("user_id")
    @Expose
    val userId: Int?,
    @SerializedName("video_id")
    @Expose
    val videoId: Int?
)