package com.madison.move.data.model

class MoveVideo(
    val id: Int,
    val title: String,
    val thumbnail: Int,
    val urlVideo: String,
    val description: String,
    val level: Int,
    val duration: Int,
    val tagId: Int,
    val status: Int,
    val view: Int,
    val time: Int,
    val category: Category,
    val user:User,
    val comment:Boolean = false
) {
}