package com.madison.move.data

import MoveRepository
import android.content.SharedPreferences
import com.madison.move.data.source.remote.MoveRemoteDataSource
import com.madison.move.data.source.remote.services.MoveApi
import com.madison.move.data.source.remote.services.MoveService

class DataManager private constructor() {

    /*val movieRepository: MoveRepository
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