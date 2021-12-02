package com.example.trucktrackingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
  private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            m1.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToTruckRegistration(10)
                findNavController().navigate(action)
            }
            m2.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToTruckRegistration(11)
                findNavController().navigate(action)
            }
        }
    }
}