package com.madison.move.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ObjectResponse<T>(
    @Expose @SerializedName("data") val data: T?,
    @Expose @SerializedName("status_code") val statusCode: Int?,
    @Expose @SerializedName("token") val token: String?,
    @Expose @SerializedName("message") val message: String?,
    @Expose @SerializedName("success") val success: Boolean?
)