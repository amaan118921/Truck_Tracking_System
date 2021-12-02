package com.example.trucktrackingsystem

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trucktrackingsystem.adapter.TruckAdapter
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.model.TruckWork


@BindingAdapter("submitList")
fun submit(recyclerView: RecyclerView, list: List<Model>?) {
val adapter = recyclerView.adapter as TruckAdapter
adapter.submitList(list)
}