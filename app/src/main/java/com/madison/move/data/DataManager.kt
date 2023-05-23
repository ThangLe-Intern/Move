package com.madison.move.data

class DataManager private constructor() {

    /*val movieRepository: com.madison.move.data.source.MoveRepository
        get() {
            val movieApi: MoveApi = MoveService.getInstance().getMovieApi()
            val movieRemote: MoveRemoteDataSource = MoveRemoteDataSource.getInstance(movieApi)
            val movieDao: MoveDao = MoveDatabase.getInstance().movieDao()
            val movieLocal: MoveLocalDataSource = MoveLocalDataSource.getInstance(movieDao)
            val movieCache: MoveCacheDataSource = MoveCacheDataSource.getsInstance()
            return MovesRepository.getInstance(movieRemote, movieLocal, movieCache)
        }*/

    companion object {
        private var sInstance: DataManager? = null

        @get:Synchronized
        val instance: DataManager?
            get() {
                if (sInstance == null) {
                    sInstance = DataManager()
                }
                return sInstance
            }
    }
}