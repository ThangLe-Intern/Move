package com.madison.move.data.source.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.madison.move.App
import com.madison.move.data.model.Video
import com.madison.move.data.source.local.MoveDao


@Database(entities = [Video::class], version = 1, exportSchema = false)
abstract class MoveDatabase : RoomDatabase() {
    abstract fun movieDao(): MoveDao

    companion object {
        private var sInstance: MoveDatabase? = null
        val instance: MoveDatabase
            get() {
                if (sInstance == null) {
                    sInstance = App.instance?.let {
                        Room.databaseBuilder(
                            it,
                            MoveDatabase::class.java,
                            "Movie.db"
                        ).build()
                    }
                }
                return sInstance!!
            }
    }
}