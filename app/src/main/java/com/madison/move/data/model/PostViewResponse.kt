package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PostViewResponse(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("video_id")
    @Expose
    val videoId: String?
)