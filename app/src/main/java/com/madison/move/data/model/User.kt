package com.madison.move.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Int,
    var username: String,
    var email: String,
    var fullname: String,
    var password: String,
    var avatar:Int,
    var email_verify_at:Int,
    var gender:String,
    var dob:String,
    var suspend:Int,
    var address:String,
    var role:Boolean = false,
    var kol:Boolean = false,
    var actived:Boolean = false,
    var status_all_notification:Boolean = false,
    var status_comment_noti:Boolean = false,
    var status_followers:Boolean = false
) : Parcelable {
}