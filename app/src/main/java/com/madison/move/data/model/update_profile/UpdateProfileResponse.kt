package com.madison.move.data.model.update_profile


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class UpdateProfileResponse(
    @SerializedName("message")
    @Expose
    val message: String?,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int?,
    @SerializedName("success")
    @Expose
    val success: Boolean?
)