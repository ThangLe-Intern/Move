package com.madison.move.data.model.videodetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoDetailResponse(
    @Expose
    @SerializedName("posts")
    val posts: Posts,
    @Expose
    @SerializedName("status")
    val status: Int
)