package com.madison.move.data.model.videodetail


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Rate(
    @SerializedName("average_rating")
    @Expose
    val averageRating: String?,
    @SerializedName("video_id")
    @Expose
    val videoId: Int?
)