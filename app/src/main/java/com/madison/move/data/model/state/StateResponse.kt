package com.madison.move.data.model.state


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class StateResponse(
    @SerializedName("data")
    @Expose
    val data: List<DataState>?,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int?,
    @SerializedName("success")
    @Expose
    val success: Boolean?
)