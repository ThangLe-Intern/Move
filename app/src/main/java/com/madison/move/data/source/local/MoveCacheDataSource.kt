package com.madison.move.data.source.local

import android.util.SparseArray
import com.madison.move.data.model.Video
import com.madison.move.data.source.MoveDataSource


class MoveCacheDataSource : MoveDataSource {

    private val cachedMovies: SparseArray<Video> = SparseArray<Video>()

    companion object {
        private var sInstance: MoveCacheDataSource? = null
        fun getsInstance(): MoveCacheDataSource? {
            if (sInstance == null) {
                sInstance = MoveCacheDataSource()
            }
            return sInstance
        }
    }

    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        if (cachedMovies.size() > 0) {
            val videos: MutableList<Video> = ArrayList<Video>()
            for (i in 0 until cachedMovies.size()) {
                val key = cachedMovies.keyAt(i)
                videos.add(cachedMovies[key])
            }
            callback?.onVideosLoaded(videos)
        } else {
            callback?.onDataNotAvailable()
        }
    }

    override fun saveVideos(videos: List<Video?>?) {
        cachedMovies.clear()
        if (videos != null) {
            for (video in videos) {
                video?.let { it.id?.let { it1 -> cachedMovies.put(it1, video) } }
            }
        }
    }
}