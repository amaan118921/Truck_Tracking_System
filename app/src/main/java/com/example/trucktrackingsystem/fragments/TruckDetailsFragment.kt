package com.example.trucktrackingsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trucktrackingsystem.MyApplication
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.adapter.TruckAdapter
import com.example.trucktrackingsystem.databinding.FragmentTruckDetailsBinding
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory


class TruckDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTruckDetailsBinding
    private lateinit var adapter: TruckAdapter
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
        binding = FragmentTruckDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.model = model
        binding.lifecycleOwner = this.viewLifecycleOwner
        setUpRecyclerView()
        model.getCacheData(model.getS(), model.getD(), model.getDate())
        binding.toolbar.title = "Truck Details"
    }

    private fun setUpRecyclerView() {
        binding.apply {
            adapter = TruckAdapter(requireParentFragment(), this@TruckDetailsFragment.model)
            recyclerView.adapter = adapter
        }
    }
}