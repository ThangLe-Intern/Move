package com.madison.move.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "videos")
class Video(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,
    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?
) : Parcelable