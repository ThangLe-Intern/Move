package com.madison.move.ui.offlinechannel

class Comment(
    val id: Int,
    val content: String,
    val timeOfComment: String,
    var listChild: MutableList<Comment>,
    val user: DataModelComment,
    var isChild: Boolean = false,
    var isLiked: Boolean = false,
    var isDiskLiked: Boolean = false,
    var isRepliesVisible: Boolean = false

) {
}