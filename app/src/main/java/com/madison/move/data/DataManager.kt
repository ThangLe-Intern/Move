package com.madison.move.data

import com.madison.move.data.source.MoveRepository
import com.madison.move.data.source.local.MoveCacheDataSource
import com.madison.move.data.source.local.MoveDao
import com.madison.move.data.source.local.MoveLocalDataSource
import com.madison.move.data.source.local.database.MoveDatabase
import com.madison.move.data.source.remote.MoveRemoteDataSource
import com.madison.move.data.source.remote.services.MoveApi
import com.madison.move.data.source.remote.services.MoveService

class DataManager private constructor() {

    val movieRepository: MoveRepository
        get() {

            val movieApi: MoveApi = MoveService.getInstance().getMovieApi()
            val movieRemote: MoveRemoteDataSource = MoveRemoteDataSource.getInstance(movieApi)

            val movieDao: MoveDao = MoveDatabase.instance.movieDao()
            val movieLocal: MoveLocalDataSource = MoveLocalDataSource.getInstance(movieDao)

            val movieCache: MoveCacheDataSource = MoveCacheDataSource.getsInstance()
            return MoveRepository.getInstance(movieRemote, movieLocal, movieCache)
        }

    companion object {
        private var sInstance: DataManager? = null

        @get:Synchronized
        val instance: DataManager
            get() {
                if (sInstance == null) {
                    sInstance = DataManager()
                }
                return sInstance!!
            }
    }
}