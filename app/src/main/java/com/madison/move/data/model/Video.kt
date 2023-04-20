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
class Video : Parcelable{
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id = 0

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    private val description: String? = null

    @Expose
    @SerializedName("image")
    @ColumnInfo(name = "image")
    private val image: String? = null

    @Expose
    @SerializedName("title")
    @ColumnInfo(name = "title")
    private val title: String? = null
}