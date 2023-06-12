package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataFAQ(
    @SerializedName("content")
    @Expose
    val content: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?
)