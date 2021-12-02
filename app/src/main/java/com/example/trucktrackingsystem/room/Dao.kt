package com.example.trucktrackingsystem.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckLocations
import com.example.trucktrackingsystem.model.TruckWork
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Flow




@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocData(list: List<TruckLocations>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Model>)

    @Query("SELECT * FROM Model where source==:source and destination==:dest and date==:date")
    fun getAll(source: String, dest: String, date:String): kotlinx.coroutines.flow.Flow<List<Model>>


    @Query("SELECT * FROM TruckLocations Where uid==:uid")
    fun getLocations(uid: String): kotlinx.coroutines.flow.Flow<TruckLocations>



}