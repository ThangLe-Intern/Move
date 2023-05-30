package com.madison.move.data.model.category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @Expose
    @SerializedName("data")
    val data: List<DataCategory>?,
    @Expose
    @SerializedName("status_code")
    val statusCode: Int?,
    @Expose
    @SerializedName("success")
    val success: Boolean?
)