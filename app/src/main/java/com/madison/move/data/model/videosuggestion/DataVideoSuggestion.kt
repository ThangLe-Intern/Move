package com.madison.move.data.model.videosuggestion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataVideoSuggestion(
    @Expose
    @SerializedName("category_name")
    val categoryName: String?,
    @Expose
    @SerializedName("created_at")
    val createdAt: String?,
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
    @SerializedName("posted_day_ago")
    val postedDayAgo: Int?,
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
    @SerializedName("total_view")
    val totalView: Int?,
    @Expose
    @SerializedName("count_view")
    val countView: Int?,
    @Expose
    @SerializedName("username")
    val username: String?

)