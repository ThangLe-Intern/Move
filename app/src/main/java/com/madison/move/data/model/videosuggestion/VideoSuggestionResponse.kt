package com.madison.move.data.model.videosuggestion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoSuggestionResponse(
    @Expose
    @SerializedName("data")
    val videoSuggestion: VideoSuggestion?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean?
)