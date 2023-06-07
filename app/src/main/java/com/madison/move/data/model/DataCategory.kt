package com.madison.move.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataCategory(
    @Expose
    @SerializedName("id")
    val id: Int?,
    @Expose
    @SerializedName("img")
    val img: String?,
    @Expose
    @SerializedName("name")
    val name: String?,
    @Expose
    @SerializedName("view_count")
    val viewCount: Int?
)