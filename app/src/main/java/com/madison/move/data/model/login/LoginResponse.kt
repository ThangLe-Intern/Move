package com.madison.move.data.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.madison.move.data.model.user_profile.DataUser

data class LoginResponse(
    @Expose
    @SerializedName("data")
    val dataUserLogin: DataUser?,
    @Expose
    @SerializedName("message")
    val message: String?,
    @Expose
    @SerializedName("statusCode")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean?,
    @Expose
    @SerializedName("token")
    val token: String?
)