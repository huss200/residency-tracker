package com.hussainali.residencytracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "student_name") val studentName: String,
    @ColumnInfo(name = "degree_name") val degreeName: String,
    @ColumnInfo(name = "department") val department: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "target_days") val targetDays: Int
)
