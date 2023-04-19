package com.madison.move.data.source.local.database

import android.arch.persistence.room.Database

/**
 * Created by Ali Asadi on 30/01/2019.
 */
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao?

    companion object {
        private var sInstance: MovieDatabase? = null
        val instance: MovieDatabase?
            get() {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                        App.getInstance(),
                        MovieDatabase::class.java,
                        "Movie.db"
                    ).build()
                }
                return sInstance
            }
    }
}