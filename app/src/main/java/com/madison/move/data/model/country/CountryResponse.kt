package com.madison.move.data.model.country


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("data")
    @Expose
    val dataCountry: List<DataCountry>?,
    @SerializedName("status_code")
    @Expose
    val statusCode: Int?,
    @SerializedName("success")
    @Expose
    val success: Boolean?
)