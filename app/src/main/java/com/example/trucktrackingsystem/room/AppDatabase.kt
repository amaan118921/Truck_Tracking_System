package com.example.trucktrackingsystem.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckLocations


@Database(entities = [Model::class, TruckLocations::class], exportSchema = false, version = 6)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "truck_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }

    }
}