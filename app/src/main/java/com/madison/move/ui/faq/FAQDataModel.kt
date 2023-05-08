package com.madison.move.ui.faq

import android.os.Parcelable
import android.widget.LinearLayout
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FAQDataModel( var title : String,var plus : Int, var minus : String): Parcelable {
}