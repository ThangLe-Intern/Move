package com.madison.move.data.model.videodetail


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Video(
    @SerializedName("category_id")
    @Expose
    val categoryId: Int?,
    @SerializedName("commentable")
    @Expose
    val commentable: Int?,
    @SerializedName("conversion")
    @Expose
    val conversion: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("duration")
    @Expose
    val duration: Int?,
    @SerializedName("featured")
    @Expose
    val featured: Int?,
    @SerializedName("file_duration")
    @Expose
    val fileDuration: String?,
    @SerializedName("file_status")
    @Expose
    val fileStatus: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("level")
    @Expose
    val level: Int?,
    @SerializedName("rates")
    @Expose
    val rates: List<Rate?>?,
    @SerializedName("show")
    @Expose
    val show: Int?,
    @SerializedName("status")
    @Expose
    val status: Int?,
    @SerializedName("tag")
    @Expose
    val tag: String?,
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?,
    @SerializedName("url_video")
    @Expose
    val urlVideo: Int?,
    @SerializedName("user_id")
    @Expose
    val userId: Int?,
    @SerializedName("view_videos")
    @Expose
    val viewVideos: List<ViewVideo?>?
)