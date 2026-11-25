package com.hussainali.residencytracker.bridge

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hussainali.residencytracker.data.ResidencyDao
import com.hussainali.residencytracker.data.ResidencyDatabase
import com.hussainali.residencytracker.data.TripEntity
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class AndroidBridgeTest {
    private lateinit var context: Context
    private lateinit var db: ResidencyDatabase
    private lateinit var dao: ResidencyDao
    private var exportedFile: File? = null
    private var printTriggered = false
    private lateinit var bridge: AndroidBridge

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, ResidencyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.residencyDao()
        bridge = AndroidBridge(
            context = context,
            dao = dao,
            onExportFile = { exportedFile = it },
            onPrintReport = { printTriggered = true }
        )
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun saveRecordsJson_rejectsMalformedJsonWithoutWipingDb() {
        dao.replaceAllTrips(listOf(TripEntity(1, "2026-01-01", "1", "2026-01-03", "1", 3, "ok")))
        val result = bridge.saveRecordsJson("{invalid-json")
        assertFalse(result)
        assertEquals(1, dao.getAllTripsSorted().size)
    }

    @Test
    fun saveAndGetSettings_roundTripsCleanly() {
        val input = JSONObject().apply {
            put("student_name", "طالب")
            put("degree_name", "دكتوراه")
            put("department", "الهندسة")
            put("country", "ألمانيا")
            put("target_days", 240)
        }.toString()

        assertTrue(bridge.saveSettingsJson(input))
        val out = JSONObject(bridge.getSettingsJson())
        assertEquals("طالب", out.getString("student_name"))
        assertEquals(240, out.getInt("target_days"))
    }

    @Test
    fun exportFile_sanitizesPathTraversal() {
        val ok = bridge.exportFile("../../evil.csv", "col1,col2\n1,2")
        assertTrue(ok)
        val file = exportedFile!!
        assertEquals("evil.csv", file.name)
        assertTrue(file.canonicalPath.startsWith(File(context.cacheDir, "exports").canonicalPath))
    }

    @Test
    fun printReport_triggersCallback() {
        bridge.printReport()
        assertTrue(printTriggered)
    }
}
