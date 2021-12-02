package com.example.trucktrackingsystem.room

import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckLocations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TruckRepository {


    private lateinit var database: FirebaseDatabase
    private lateinit var dao: Dao
    private val locList: MutableList<TruckLocations> = mutableListOf()
    private val list: MutableList<Model> = mutableListOf()
     fun refreshData(dao: Dao) {
        this.dao = dao
        fetchDataFromFirebase()
    }


   private fun fetchDataFromFirebase() {
       database = FirebaseDatabase.getInstance()
       val ref = database.reference.child("works")
       ref.addValueEventListener(object: ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
               for(data: DataSnapshot in snapshot.children) {
                   val item = data.getValue(Model::class.java)
                   list.add(item!!)
               }
            insertData()
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

       })

    }

     fun insertData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao.insertAll(list)
        }
    }

    fun refreshLocation() {
        fetchLocationData()
    }


    private fun fetchLocationData() {
        database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("locations")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data: DataSnapshot in snapshot.children) {
                    val item = data.getValue(TruckLocations::class.java)
                    locList.add(item!!)
                }
                insertLocData()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun insertLocData() {
        GlobalScope.launch(Dispatchers.IO) {
            dao.insertLocData(locList)
        }
    }

}