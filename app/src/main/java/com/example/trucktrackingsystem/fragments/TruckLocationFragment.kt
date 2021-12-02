package com.example.trucktrackingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.trucktrackingsystem.MyApplication
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentTruckLocationBinding
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory


class TruckLocationFragment : Fragment() {
  private lateinit var binding: FragmentTruckLocationBinding

    private val model: TruckViewModel by activityViewModels {
        TruckViewModelFactory(
            (activity?.application as MyApplication).database.getDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTruckLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.model = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        val model = model.getModel()
        this.model.getLocationCacheData(model.uid)

        binding.apply {
            driverName.text = model.driverName
            truckType.text = model.addDetails
            sText.text = model.source
            dText.text =  model.destination
        }

        binding.mapLoc.setOnClickListener {
            val lat = binding.latValue.text.toString()
            val lon = binding.lonValue.text.toString()
            if(lat.isNotEmpty() && lon.isNotEmpty()) {
                val action = TruckLocationFragmentDirections.actionTruckLocationFragmentToMapsFragment(lat, lon)
                findNavController().navigate(action)
            }
            else {

            }
        }



    }
}