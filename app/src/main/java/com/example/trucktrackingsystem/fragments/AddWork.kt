package com.example.trucktrackingsystem.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trucktrackingsystem.MyApplication
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentAddWorkBinding
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckWork
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class AddWork : Fragment() {
  private lateinit var binding: FragmentAddWorkBinding
  private val AUTOCOMPLETE_REQUEST_CODE = 1
  private lateinit var auth: FirebaseAuth
  private lateinit var database: FirebaseDatabase
  private lateinit var selectedDate: String
  private lateinit var dialog: ProgressDialog
  private lateinit var key: String
  val range = (1..90000)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWorkBinding.inflate(inflater)
        return binding.root
    }


    private val model: TruckViewModel by activityViewModels {
        TruckViewModelFactory(
            (activity?.application as MyApplication).database.getDao()
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpDialog()
        model.getName()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.apply {
            selectDate.setOnClickListener {
                setDate()
            }
//            sourceLoc.setOnClickListener {
//                key = "1"
//                openActivity()
//            }
//            destinationLoc.setOnClickListener {
//                key = "0"
//                openActivity()
//            }
            done.setOnClickListener {
                if(sText.text.toString().isEmpty() || dText.text.toString().isEmpty() || addDetailsEdt.text.isNullOrEmpty() || dateText.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Enter all fields", Toast.LENGTH_SHORT).show()
                }
                else {
                    show()
                    val ref = database.reference.child("works")
                    val truckWork = Model(driverName = model.name, addDetails = addDetailsEdt.text.toString(),
                    source = sText.text.toString(), destination = dText.text.toString(), date = dateText.text.toString(), uid = auth.uid!!, id=range.random())
                        ref.push().setValue(truckWork).addOnCompleteListener(object: OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if(p0.isSuccessful) {
                                    dismiss()
                                    findNavController().navigate(R.id.action_addWork2_to_truckMainFragment)
                                }
                            }

                        })
                }
            }
        }
    }


    private fun openActivity() {

        Places.initialize(requireContext(), getString(R.string.api_key));

        val fields = listOf(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        if(key=="1") {
                            binding.sText.setText(place.name)
                        }
                        else {
                            binding.dText.setText(place.name)
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(requireContext(), status.statusMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
    }


    private fun setDate() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                selectedDate = "0$dayOfMonth/${month+1}/$year"
                binding.dateText.setText(selectedDate)
            }

        }, year, month, day)
        datePickerDialog.show()
    }

    private fun setUpDialog() {
        dialog = ProgressDialog(requireContext())
        dialog.setTitle("Please wait..")
        dialog.setCancelable(false)
    }

    private fun show() {
        dialog.show()
    }
    private fun dismiss() {
        dialog.dismiss()
    }
}