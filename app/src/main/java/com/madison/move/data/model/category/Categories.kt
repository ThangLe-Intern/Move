package com.madison.move.data.model.category

data class Categories(
    val current_page: Int,
    val `data`: List<DataCategory>,
    val first_page_url: String,
    val from: Int,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: String,
    val to: Int
)