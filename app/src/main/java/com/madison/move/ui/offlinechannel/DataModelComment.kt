package com.madison.move.ui.offlinechannel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModelComment (val avt:Int,val name:String,val isTicked:Boolean = false):Parcelable