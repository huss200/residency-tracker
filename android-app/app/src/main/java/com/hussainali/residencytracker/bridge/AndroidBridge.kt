package com.hussainali.residencytracker.bridge

import android.content.Context
import android.webkit.JavascriptInterface
import com.hussainali.residencytracker.data.ResidencyDao
import com.hussainali.residencytracker.data.SettingsEntity
import com.hussainali.residencytracker.data.TripEntity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AndroidBridge(
    private val context: Context,
    private val dao: ResidencyDao,
    private val onExportFile: (File) -> Unit,
    private val onPrintReport: () -> Unit
) {

    @JavascriptInterface
    fun getRecordsJson(): String {
        val arr = JSONArray()
        for (trip in dao.getAllTripsSorted()) {
            arr.put(
                JSONObject().apply {
                    put("id", trip.id)
                    put("exit_date", trip.exitDate)
                    put("exit_page", trip.exitPage)
                    put("entry_date", trip.entryDate)
                    put("entry_page", trip.entryPage)
                    put("days", trip.days)
                    put("notes", trip.notes)
                }
            )
        }
        return arr.toString()
    }

    @JavascriptInterface
    fun saveRecordsJson(json: String): Boolean {
        return try {
            val arr = JSONArray(json)
            val parsed = ArrayList<TripEntity>(arr.length())
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                parsed.add(
                    TripEntity(
                        id = obj.getLong("id"),
                        exitDate = obj.optString("exit_date", ""),
                        exitPage = obj.optString("exit_page", ""),
                        entryDate = obj.optString("entry_date", ""),
                        entryPage = obj.optString("entry_page", ""),
                        days = obj.optInt("days", 0),
                        notes = obj.optString("notes", "")
                    )
                )
            }
            dao.replaceAllTrips(parsed)
            true
        } catch (_: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun getSettingsJson(): String {
        val s = dao.getSettings() ?: return "{}"
        return JSONObject().apply {
            put("student_name", s.studentName)
            put("degree_name", s.degreeName)
            put("department", s.department)
            put("country", s.country)
            put("target_days", s.targetDays)
        }.toString()
    }

    @JavascriptInterface
    fun saveSettingsJson(json: String): Boolean {
        return try {
            val obj = JSONObject(json)
            dao.upsertSettings(
                SettingsEntity(
                    id = 1,
                    studentName = obj.optString("student_name", ""),
                    degreeName = obj.optString("degree_name", "ماجستير"),
                    department = obj.optString("department", ""),
                    country = obj.optString("country", "بلد الدراسة والابتعاث"),
                    targetDays = obj.optInt("target_days", 180)
                )
            )
            true
        } catch (_: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun exportFile(filename: String, content: String): Boolean {
        return try {
            val safeName = File(filename).name.ifBlank { "export.txt" }
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val outFile = File(exportDir, safeName)
            outFile.writeText(content, Charsets.UTF_8)
            onExportFile(outFile)
            true
        } catch (_: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun printReport() {
        onPrintReport()
    }
}
