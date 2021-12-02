package com.example.trucktrackingsystem.viewModel

import androidx.lifecycle.*
import com.example.trucktrackingsystem.fragments.TruckRegistration
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckLocations
import com.example.trucktrackingsystem.model.TruckRegister
import com.example.trucktrackingsystem.model.UserDetails
import com.example.trucktrackingsystem.room.TruckRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TruckViewModel(private val dao: com.example.trucktrackingsystem.room.Dao): ViewModel() {

     private lateinit var auth: FirebaseAuth
     private lateinit var database: FirebaseDatabase
     val _name = MutableLiveData<String>()
     val _phone = MutableLiveData<String>()
     val _truckType = MutableLiveData<String>()
     val _truckNumber = MutableLiveData<String>()
     val _email = MutableLiveData<String>()
     var name: String = ""
    private lateinit var date: String
     lateinit var _data: LiveData<List<Model>>
    private lateinit var source: String
    private lateinit var dest: String
    private val repository = TruckRepository()
    private lateinit var model: Model

     lateinit var _locationData : LiveData<TruckLocations>

init {
    refreshDataFromViewModel()
    refreshLocationData()
}
    fun setSource(source: String) {
        this.source = source
    }

    fun setDest(dest: String) {
        this.dest = dest
    }

    fun getS(): String {
        return source
    }

    fun getD() : String {
        return dest
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getDate(): String {
        return date
    }


    fun setModel(model: Model) {
        this.model = model
    }

    fun getModel(): Model {
        return model
    }
    private fun refreshDataFromViewModel() {
        viewModelScope.launch {
            repository.refreshData(dao)
        }
    }

    fun getLocationCacheData(uid: String) {
        viewModelScope.launch {
            _locationData = dao.getLocations(uid).asLiveData()
        }
    }

    private fun refreshLocationData() {
        viewModelScope.launch {
            repository.refreshLocation()
        }
    }



    private val c = Calendar.getInstance().time
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val formattedDate = df.format(c)



    fun getCacheData(source: String, dest: String, date: String) {
        viewModelScope.launch {
            _data = dao.getAll(source, dest, date).asLiveData()
        }
    }



    fun getName() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("truckDetails").child(auth.uid!!)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun pushData(truckUser: TruckRegister) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val ref = database.reference.child("truckDetails").child(auth.uid!!)

       viewModelScope.launch(Dispatchers.IO) {
           val res = ref.setValue(truckUser)
           if(res.isSuccessful) {
               TruckRegistration.Dialog.dismiss()
           }
           else {
               TruckRegistration.Dialog.dismiss()
           }
        }



    }
    fun pushUserDetails(userDetails: UserDetails) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val ref = database.reference.child("userDetails").child(auth.uid!!)
        viewModelScope.launch {
           val res = withContext(Dispatchers.Default) {
               ref.setValue(userDetails)
           }
           if(res.isSuccessful) {
               TruckRegistration.Dialog.dismiss()
           }
        }


    }

 fun getProfileDetails() {
     auth = FirebaseAuth.getInstance()
     database = FirebaseDatabase.getInstance()
     val ref = database.reference.child("truckDetails").child(auth.uid!!)
     ref.addValueEventListener(object: ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
             _name.value = snapshot.child("name").value.toString()
             _phone.value = snapshot.child("phone").value.toString()
             _email.value = snapshot.child("email").value.toString()
             _truckNumber.value = snapshot.child("truckNumber").value.toString()
             _truckType.value = snapshot.child("truckType").value.toString()
         }

         override fun onCancelled(error: DatabaseError) {
             TODO("Not yet implemented")
         }

     })
 }


}






class TruckViewModelFactory(private val dao: com.example.trucktrackingsystem.room.Dao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TruckViewModel::class.java)) {
            return TruckViewModel(dao) as T
        }
        else {
            throw IllegalArgumentException("Unknown")
        }
    }

}