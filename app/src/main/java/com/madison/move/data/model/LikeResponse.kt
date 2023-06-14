package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LikeResponse(
    @SerializedName("data")
    @Expose
    val data: Boolean?,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int?,
    @SerializedName("success")
    @Expose
    val success: String?
)