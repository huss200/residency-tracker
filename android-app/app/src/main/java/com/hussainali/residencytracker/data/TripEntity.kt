package com.hussainali.residencytracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "exit_date") val exitDate: String,
    @ColumnInfo(name = "exit_page") val exitPage: String,
    @ColumnInfo(name = "entry_date") val entryDate: String,
    @ColumnInfo(name = "entry_page") val entryPage: String,
    @ColumnInfo(name = "days") val days: Int,
    @ColumnInfo(name = "notes") val notes: String
)
