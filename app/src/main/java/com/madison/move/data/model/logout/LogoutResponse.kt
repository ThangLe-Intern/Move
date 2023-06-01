package com.madison.move.data.model.logout


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LogoutResponse(
    @SerializedName("message")
    @Expose
    val message: String?,
    @SerializedName("success")
    @Expose
    val success: Boolean?
)