package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataUser(
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("birthday")
    @Expose
    val birthday: String?,
    @SerializedName("country_id")
    @Expose
    val countryId: Int?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("fullname")
    @Expose
    val fullname: String?,
    @SerializedName("gender")
    @Expose
    val gender: Int?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("img")
    @Expose
    val img: Any?,
    @SerializedName("kol")
    @Expose
    val kol: Int?,
    @SerializedName("state_id")
    @Expose
    val stateId: Int?,
    @SerializedName("user_id")
    @Expose
    val userId: Int?,
    @SerializedName("username")
    @Expose
    val username: String?
)