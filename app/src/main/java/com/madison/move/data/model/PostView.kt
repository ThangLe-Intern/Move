package com.madison.move.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostView(
    @SerializedName("time")
    @Expose
    val time: String?
)

