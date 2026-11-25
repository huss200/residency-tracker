package com.hussainali.residencytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TripEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ResidencyDatabase : RoomDatabase() {
    abstract fun residencyDao(): ResidencyDao

    companion object {
        @Volatile
        private var INSTANCE: ResidencyDatabase? = null

        fun getInstance(context: Context): ResidencyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ResidencyDatabase::class.java,
                    "residency_tracker.db"
                )
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
