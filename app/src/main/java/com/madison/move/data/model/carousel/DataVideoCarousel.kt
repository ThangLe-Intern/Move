package com.madison.move.data.model.carousel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataVideoCarousel(
    @Expose
    @SerializedName("category_name")
    val categoryName: String?,
    @Expose
    @SerializedName("count_view")
    val countView: Int?,
    @Expose
    @SerializedName("duration")
    val duration: Int?,
    @Expose
    @SerializedName("id")
    val id: Int?,
    @Expose
    @SerializedName("img")
    val img: String?,
    @Expose
    @SerializedName("level")
    val level: Int?,
    @Expose
    @SerializedName("rating")
    val rating: Double?,
    @Expose
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @Expose
    @SerializedName("title")
    val title: String?,
    @Expose
    @SerializedName("username")
    val username: String?
)