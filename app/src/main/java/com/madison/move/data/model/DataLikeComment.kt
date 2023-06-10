package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataLikeComment(
    @SerializedName("comment_id")
    @Expose
    val commentId: Int?,
    @SerializedName("dislike_count")
    @Expose
    val dislikeCount: Int?,
    @SerializedName("is_disliked")
    @Expose
    val isDisliked: String?,
    @SerializedName("is_liked")
    @Expose
    val isLiked: String?,
    @SerializedName("like_count")
    @Expose
    val likeCount: Int?
)