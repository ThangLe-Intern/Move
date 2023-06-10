package com.madison.move.data.model.videodetail


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ViewVideo(
    @SerializedName("age")
    @Expose
    val age: Int?,
    @SerializedName("country_id")
    @Expose
    val countryId: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("gender")
    @Expose
    val gender: Int?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("state_id")
    @Expose
    val stateId: Int?,
    @SerializedName("time")
    @Expose
    val time: Int?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?,
    @SerializedName("user_id")
    @Expose
    val userId: Int?,
    @SerializedName("video_id")
    @Expose
    val videoId: Int?
)