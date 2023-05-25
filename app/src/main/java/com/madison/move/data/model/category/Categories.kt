package com.madison.move.data.model.category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Categories(
    @Expose
    @SerializedName("current_page")
    val currentPage: Int,
    @Expose
    @SerializedName("data")
    val `data`: List<DataCategory>,
    @Expose
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @Expose
    @SerializedName("from")
    val from: Int,
    @Expose
    @SerializedName("next_page_url")
    val nextPageUrl: String,
    @Expose
    @SerializedName("path")
    val path: String,
    @Expose
    @SerializedName("per_page")
    val perPage: Int,
    @Expose
    @SerializedName("prev_page_url")
    val prevPageUrl: String,
    @Expose
    @SerializedName("to")
    val to: Int

)