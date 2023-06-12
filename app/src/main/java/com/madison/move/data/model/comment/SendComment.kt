package com.madison.move.data.model.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendComment(
    @SerializedName("content")
    @Expose
    val content: String?
)