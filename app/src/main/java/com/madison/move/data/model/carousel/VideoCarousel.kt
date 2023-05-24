package com.madison.move.data.model.carousel

data class VideoCarousel(
    val current_page: Int,
    val `data`: List<DataVideoCarousel>,
    val first_page_url: String,
    val from: Int,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int
)