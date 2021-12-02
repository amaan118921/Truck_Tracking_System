package com.example.trucktrackingsystem.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TruckLocations(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "lat") @NonNull val lat: String = "",
    @ColumnInfo(name = "lon") @NonNull val lon: String = "",
    @ColumnInfo(name = "uid") @NonNull val uid: String = ""
)