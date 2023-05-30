package com.madison.move.data.model.country

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataCountry(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("name")
    @Expose
    val name: String?
)