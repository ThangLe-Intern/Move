package com.madison.move.data.model.videodetail


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataVideoDetail(
    @SerializedName("category_name")
    @Expose
    val categoryName: String?,
    @SerializedName("commentable")
    @Expose
    val commentable: Int?,
    @SerializedName("duration")
    @Expose
    val duration: Int?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("level")
    @Expose
    val level: Int?,
    @SerializedName("ratings")
    @Expose
    val ratings: Double?,
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("url_video")
    @Expose
    val urlVideo: Int?,
    @SerializedName("user_id")
    @Expose
    val userId: Int?,
    @SerializedName("views")
    @Expose
    val views: Long?
)