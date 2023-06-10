package com.madison.move.data.model.videodetail


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataVideoDetail(
    @SerializedName("averageRating")
    @Expose
    val averageRating: String?,
    @SerializedName("video")
    @Expose
    val video: Video?,
    @SerializedName("viewsCount")
    @Expose
    val viewsCount: Int?
)