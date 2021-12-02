package com.example.trucktrackingsystem

import android.app.Application
import com.example.trucktrackingsystem.room.AppDatabase

class MyApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}