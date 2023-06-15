package com.madison.move.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DataUser(
    @SerializedName("active")
    @Expose
    val active: Int?,
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("birthday")
    @Expose
    val birthday: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("email_verified_at")
    @Expose
    val emailVerifiedAt: String?,
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
    var img: String?,
    @SerializedName("is_suspended")
    @Expose
    val isSuspended: Int?,
    @SerializedName("kol")
    @Expose
    val kol: Int?,
    @SerializedName("role")
    @Expose
    val role: Int?,
    @SerializedName("status_all_notification")
    @Expose
    val statusAllNotification: Int?,
    @SerializedName("status_comment_notification")
    @Expose
    val statusCommentNotification: Int?,
    @SerializedName("status_follow_notification")
    @Expose
    val statusFollowNotification: Int?,
    @SerializedName("suspended_until")
    @Expose
    val suspendedUntil: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?,
    @SerializedName("username")
    @Expose
    var username: String?,
    @SerializedName("country_id")
    @Expose
    val countryId: Int?,

    @SerializedName("state_id")
    @Expose
    val stateId: Int?,

    @SerializedName("user_id")
    @Expose
    val userId: Int?
)