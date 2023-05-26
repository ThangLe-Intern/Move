package com.madison.move.data.model.carousel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CarouselResponse(
    @Expose
    @SerializedName("data")
    val data: List<DataVideoCarousel>?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean
)