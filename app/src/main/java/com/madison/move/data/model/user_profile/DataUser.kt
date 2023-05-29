package com.madison.move.data.model.user_profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataUser(
    @Expose
    @SerializedName("address")
    val address: String?,
    @Expose
    @SerializedName("birthday")
    val birthday: String?,
    @Expose
    @SerializedName("email")
    val email: String?,
    @Expose
    @SerializedName("fullname")
    val fullName: String?,
    @Expose
    @SerializedName("gender")
    val gender: Int?,
    @Expose
    @SerializedName("id")
    val id: Int?,
    @Expose
    @SerializedName("img")
    val img: String?,
    @Expose
    @SerializedName("kol")
    val kol: Int?,
    @Expose
    @SerializedName("username")
    val username: String?
)