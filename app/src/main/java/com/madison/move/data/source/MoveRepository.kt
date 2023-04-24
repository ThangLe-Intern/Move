
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.local.MoveCacheDataSource
import com.madison.move.data.source.local.MoveLocalDataSource
import com.madison.move.data.source.remote.MoveRemoteDataSource


class MoveRepository private constructor(
    private val movieRemote: MoveDataSource,
    private val movieLocal: MoveDataSource,
    private val movieCache: MoveDataSource,
) : MoveDataSource {




    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        if (callback == null) return
        movieCache.getVideos(object : MoveDataSource.LoadVideosCallback {

            override fun onVideosLoaded(videos: List<com.madison.move.data.model.Video?>?) {
                callback.onVideosLoaded(videos)
            }

            override fun onDataNotAvailable() {
            }

            override fun onError() {
            }

        })
    }

    override fun saveVideos(videos: List<com.madison.move.data.model.Video?>?) {
        movieLocal.saveVideos(videos)
    }



    private fun getMoviesFromLocalDataSource(callback: MoveDataSource.LoadVideosCallback) {
        movieLocal.getVideos(object : MoveDataSource.LoadVideosCallback {

            override fun onVideosLoaded(videos: List<com.madison.move.data.model.Video?>?) {
                callback.onVideosLoaded(videos)
                refreshCache(videos)
            }

            override fun onDataNotAvailable() {
                getVideosFromRemoteDataSource(callback)
            }

            override fun onError() {

            }


        })
    }

    private fun getVideosFromRemoteDataSource(callback: MoveDataSource.LoadVideosCallback) {
        movieRemote.getVideos(object : MoveDataSource.LoadVideosCallback {

            override fun onVideosLoaded(videos: List<com.madison.move.data.model.Video?>?) {
                callback.onVideosLoaded(videos)
                saveVideos(videos)
                refreshCache(videos)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

            override fun onError() {
                callback.onError()
            }
        })
    }

    private fun refreshCache(movies: List<com.madison.move.data.model.Video?>?) {
        movieCache.saveVideos(movies)
    }

    fun destroyInstance() {
        instance = null
    }

    companion object {
        private var instance: MoveRepository? = null
        fun getInstance(
            movieRemote: MoveRemoteDataSource,
            movieLocal: MoveLocalDataSource,
            movieCache: MoveCacheDataSource
        ): MoveRepository? {
            if (instance == null) {
                instance = MoveRepository(movieRemote, movieLocal, movieCache)
            }
            return instance
        }
    }
}