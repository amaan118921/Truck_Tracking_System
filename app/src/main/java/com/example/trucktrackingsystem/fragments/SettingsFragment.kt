package com.example.trucktrackingsystem.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trucktrackingsystem.MyApplication
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentSettingsBinding
import com.example.trucktrackingsystem.service.TrackingService
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding


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
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner
        model.getProfileDetails()
        binding.toolbar.title = "Settings"
        val sharedPreferences = requireActivity().getSharedPreferences("gps_enabled", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.gpsSwitch.setOnClickListener {
            if(binding.gpsSwitch.isChecked) {
                editor.putString("gps","enabled")
                editor.apply()
                requireActivity().startService(Intent(requireContext(), TrackingService::class.java))
            }
            else {
                editor.putString("gps","disabled")
                editor.apply()
                requireActivity().stopService(Intent(requireContext(), TrackingService::class.java))
            }
        }
    }
}