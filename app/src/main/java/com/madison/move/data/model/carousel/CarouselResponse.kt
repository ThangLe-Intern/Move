package com.madison.move.data.model.carousel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion

data class CarouselResponse(
    @Expose
    @SerializedName("data")
    val data: List<DataVideoSuggestion>?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean?
)