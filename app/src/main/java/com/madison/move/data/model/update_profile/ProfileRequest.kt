package com.madison.move.data.model.update_profile


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.util.*

data class ProfileRequest(
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("birthday")
    @Expose
    val birthday: String?,
    @SerializedName("country_id")
    @Expose
    val countryId: Int?,
    @SerializedName("fullname")
    @Expose
    val fullname: String?,
    @SerializedName("gender")
    @Expose
    val gender: Int?,
    @SerializedName("img")
    @Expose
    val img: String?,
    @SerializedName("state_id")
    @Expose
    val stateId: Int?,
    @SerializedName("username")
    @Expose
    val username: String?
)