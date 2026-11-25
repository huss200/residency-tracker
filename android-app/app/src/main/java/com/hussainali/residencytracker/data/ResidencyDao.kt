package com.hussainali.residencytracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ResidencyDao {
    @Query("SELECT * FROM trips ORDER BY exit_date ASC, id ASC")
    fun getAllTripsSorted(): List<TripEntity>

    @Query("DELETE FROM trips")
    fun deleteAllTrips()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrips(trips: List<TripEntity>)

    @Transaction
    fun replaceAllTrips(trips: List<TripEntity>) {
        deleteAllTrips()
        if (trips.isNotEmpty()) {
            insertTrips(trips)
        }
    }

    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertSettings(settings: SettingsEntity)
}
