package com.example.trucktrackingsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.ItemTruckDetailsBinding
import com.example.trucktrackingsystem.model.Model
import com.example.trucktrackingsystem.viewModel.TruckViewModel

class TruckAdapter(private val fragment: Fragment, private val model: TruckViewModel): ListAdapter<Model, TruckAdapter.TruckViewHolder>(DiffCallback) {

    class TruckViewHolder(binding: ItemTruckDetailsBinding): RecyclerView.ViewHolder(binding.root) {
        val name = binding.driverName
        val addDetails = binding.truckType
        val source = binding.source
        val destination = binding.destination
    }

    companion object {
        val DiffCallback = object: DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.driverName==newItem.driverName
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
               return oldItem.addDetails==newItem.addDetails
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val adapter = ItemTruckDetailsBinding.inflate(LayoutInflater.from(parent.context))
        return TruckViewHolder(adapter)
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.setText(item.driverName)
        holder.addDetails.setText(item.addDetails)
        holder.source.setText(item.source)
        holder.destination.setText(item.destination)

        holder.itemView.setOnClickListener {
            model.setModel(item)
            fragment.findNavController().navigate(R.id.action_truckDetailsFragment_to_truckLocationFragment)
        }

    }
}