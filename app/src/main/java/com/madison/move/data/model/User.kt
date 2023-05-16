package com.madison.move.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Int? = 1,
    var username: String? = "",
    var email: String? = "",
    var fullname: String? = "",
    var password: String? = "",
    var avatar:Int? = 1,
    var email_verify_at:Int? = 1,
    var gender:String? = "",
    var dob:String? = "",
    var suspend:Int? = 1,
    var address:String? = "",
    var role:Boolean = false,
    var kol:Boolean = false,
    var actived:Boolean = false,
    var status_all_notification:Boolean = false,
    var status_comment_noti:Boolean = false,
    var status_followers:Boolean = false
) : Parcelable {
}