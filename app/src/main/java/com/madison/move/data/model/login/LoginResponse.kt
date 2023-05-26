package com.madison.move.data.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("message")
    val message: String?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean?,
    @Expose
    @SerializedName("token")
    val token: String?
)