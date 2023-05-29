package com.madison.move.data.model.user_profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @Expose
    @SerializedName("data")
    val dataUser: DataUser?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean
)