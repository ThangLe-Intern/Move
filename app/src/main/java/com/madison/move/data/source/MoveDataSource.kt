package com.madison.move.data.source

import com.madison.move.data.model.Video


interface MoveDataSource {
    interface LoadVideosCallback {
        fun onVideosLoaded(videos: List<Video?>?)
        fun onDataNotAvailable()
        fun onError()
    }

    fun getVideos(callback: LoadVideosCallback?)
    fun saveVideos(videos: List<Video?>?)
}