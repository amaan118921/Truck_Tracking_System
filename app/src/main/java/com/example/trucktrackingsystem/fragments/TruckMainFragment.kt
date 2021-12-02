package com.example.trucktrackingsystem.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentTruckMainBinding
import com.example.trucktrackingsystem.service.TrackingService
import java.util.jar.Manifest

class TruckMainFragment : Fragment() {

    private lateinit var binding: FragmentTruckMainBinding
    private val permissionRequest = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTruckMainBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()
        setUpTracking()
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_truckMainFragment_to_addWork22)
        }
    }


    private fun setUpToolbar() {
        binding.toolbar.title = "Your Work"
        binding.toolbar.setOnMenuItemClickListener(object: androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item!!.itemId) {
                    R.id.settings -> findNavController().navigate(R.id.action_truckMainFragment_to_settingsFragment)
                }
                return true
            }

        })
    }

    private fun setUpTracking() {
        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionRequest)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==permissionRequest && grantResults.size==1 &&
            grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        }
        else {
            Toast.makeText(requireContext(), "Permission Denied, Location Tracking cannot be started", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startTrackerService() {
        val intent = Intent(requireContext(), TrackingService::class.java)
        requireActivity().startService(intent)
        Toast.makeText(requireContext(), "Location Tracking Enabled", Toast.LENGTH_SHORT).show()
    }

}