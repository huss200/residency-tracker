package com.hussainali.residencytracker.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResidencyDaoTest {
    private lateinit var db: ResidencyDatabase
    private lateinit var dao: ResidencyDao

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, ResidencyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.residencyDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun replaceAllTrips_sortsByExitDateAndPreservesSettings() {
        dao.upsertSettings(SettingsEntity(1, "طالب تجريبي", "ماجستير", "CS", "UK", 180))
        dao.replaceAllTrips(
            listOf(
                TripEntity(2, "2026-02-01", "2", "2026-02-10", "2", 10, ""),
                TripEntity(1, "2026-01-01", "1", "2026-01-05", "1", 5, "first")
            )
        )
        val trips = dao.getAllTripsSorted()
        assertEquals(listOf("2026-01-01", "2026-02-01"), trips.map { it.exitDate })
        assertEquals("طالب تجريبي", dao.getSettings()?.studentName)
    }
}
