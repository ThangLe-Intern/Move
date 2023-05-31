package com.madison.move.data.model.videodetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Posts(
    @Expose
    @SerializedName("category_id")
    val categoryId: Int,
    @Expose
    @SerializedName("commentable")
    val commentable: Int,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("duration")
    val duration: Int,
    @Expose
    @SerializedName("featured")
    val featured: Int,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("level")
    val level: Int,
    @Expose
    @SerializedName("show")
    val show: Int,
    @Expose
    @SerializedName("status")
    val status: Int,
    @Expose
    @SerializedName("tag")
    val tag: String,
    @Expose
    @SerializedName("thumbnail")
    val thumbnail: String,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String,
    @Expose
    @SerializedName("url_video")
    val urlVideo: String,
    @Expose
    @SerializedName("user_id")
    val userId: Int

)