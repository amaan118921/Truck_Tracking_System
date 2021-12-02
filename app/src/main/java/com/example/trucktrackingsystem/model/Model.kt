package com.example.trucktrackingsystem.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Model(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name="driverName") @NonNull val driverName: String="",
    @ColumnInfo(name="addDetails") @NonNull val addDetails: String="",
    @ColumnInfo(name="source") @NonNull val source: String="",
    @ColumnInfo(name="destination") @NonNull val destination: String="",
    @ColumnInfo(name="date") @NonNull val date: String="",
    @ColumnInfo(name="uid") @NonNull val uid: String=""
)
