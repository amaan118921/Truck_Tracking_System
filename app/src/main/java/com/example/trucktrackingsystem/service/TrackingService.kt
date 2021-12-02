package com.example.trucktrackingsystem.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.trucktrackingsystem.model.TruckLocations
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.jar.Manifest

class TrackingService: Service() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private val range = (1..90000)
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        requestLocationUpdates()    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestLocationUpdates() {
        val ref = database.reference.child("locations").child(auth.uid!!)
        locationRequest = com.google.android.gms.location.LocationRequest.create().setInterval(10000).setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        val client = LocationServices.getFusedLocationProviderClient(this)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(locationRequest, object: LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    val location = p0?.lastLocation
                    if(location!=null) {
                        val truckLocations = TruckLocations(lat = location.latitude.toString(),lon = location.longitude.toString(), uid = auth.uid!!, id = range.random())
                        ref.setValue(truckLocations).addOnCompleteListener(object: OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful) {

                                }
                            }

                        })
                    }
                    else {
                        Log.d("checkForLocation", "Cannot Update")
                    }
                }
            }, null)
        }

    }

}