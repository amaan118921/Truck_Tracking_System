package com.example.trucktrackingsystem.fragments

import android.app.Activity
import android.app.DatePickerDialog
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
import com.example.trucktrackingsystem.databinding.FragmentCustomerMainBinding
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.*

class CustomerMainFragment : Fragment() {
    private lateinit var binding: FragmentCustomerMainBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 100
    private lateinit var key: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerMainBinding.inflate(inflater)
        return binding.root
    }

    private val model: TruckViewModel by activityViewModels {
        TruckViewModelFactory(
            (activity?.application as MyApplication).database.getDao()
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.toolbar.title = "Customer Portal"

        binding.selectDate.setOnClickListener {
            setDate()
        }

        binding.apply {
//            sourceLoc.setOnClickListener {
//                key = "1"
//                openActivity()
//            }
//
//            sourceLoc.setOnClickListener {
//                key = "0"
//                openActivity()
//            }

            done.setOnClickListener {

                if(binding.sText.text.toString().isEmpty() || binding.dText.text.toString().isEmpty() || binding.dateText.text.toString().isEmpty()) {

                }
                else {
                    model.setSource(binding.sText.text.toString())
                    model.setDest(binding.dText.text.toString())
                    model.setDate(binding.dateText.text.toString())
                    findNavController().navigate(R.id.action_customerMainFragment_to_truckDetailsFragment)
                }




            }
        }
    }

    private fun setDate() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = "0$dayOfMonth/${month+1}/$year"
                binding.dateText.setText(selectedDate)
            }

        }, year, month, day)
        datePickerDialog.show()
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
}