package com.example.trucktrackingsystem.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContentInfo
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trucktrackingsystem.MyApplication
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentTruckRegistrationBinding
import com.example.trucktrackingsystem.model.TruckRegister
import com.example.trucktrackingsystem.model.UserDetails
import com.example.trucktrackingsystem.service.TrackingService
import com.example.trucktrackingsystem.viewModel.TruckViewModel
import com.example.trucktrackingsystem.viewModel.TruckViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class TruckRegistration : Fragment() {
    private lateinit var binding: FragmentTruckRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private val navArgs : TruckRegistrationArgs by navArgs()
    private val model: TruckViewModel by activityViewModels {
        TruckViewModelFactory(
            (activity?.application as MyApplication).database.getDao()
        )
    }

    companion object {
        lateinit var dialog: ProgressDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTruckRegistrationBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        checkFor()
        setUpDialog()
        binding.done.setOnClickListener {
            val name = binding.driverName.text.toString()
            val phone = binding.driverPhone.text.toString()
            val truckType = binding.truckType.text.toString()
            val truckNumber = binding.truckNumber.text.toString()
            val email = binding.drvierEmail.text.toString()
            val pass = binding.password.text.toString()
            if(name.isNullOrEmpty() || phone.isNullOrEmpty() || truckType.isNullOrEmpty() || truckNumber.isNullOrEmpty()
                || email.isNullOrEmpty() || pass.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Enter all required details", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) { task->
                    if(task.isSuccessful) {
                        Dialog.show()
                        val sharedPreferences = requireActivity().getSharedPreferences("check", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("value", "1").apply()
                        val truckUser = TruckRegister(name, email, phone, truckType, truckNumber)
                        model.pushData(truckUser)
                        findNavController().navigate(R.id.action_truckRegistration_to_truckMainFragment)
                    }

                }
            }

        }

        binding.truckLogin.setOnClickListener {
            val action = TruckRegistrationDirections.actionTruckRegistrationToLoginFragment(10)
            findNavController().navigate(action)
        }
        binding.userLogin.setOnClickListener {
            val action = TruckRegistrationDirections.actionTruckRegistrationToLoginFragment(20)
            findNavController().navigate(action)
        }

        binding.userDone.setOnClickListener {
            val name =  binding.userName.text.toString()
            val phone = binding.userPhone.text.toString()
            val email = binding.userEmail.text.toString()
            if(name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Not there", Toast.LENGTH_SHORT).show()
            }
            else {
                Dialog.show()
                auth.createUserWithEmailAndPassword(email, phone).addOnCompleteListener(requireActivity()) {
                    task ->
                    if(task.isSuccessful) {
                        Toast.makeText(requireContext(), "In there", Toast.LENGTH_SHORT).show()
                        val sharedPreferences = requireActivity().getSharedPreferences("check", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("value", "0").apply()
                        val user = UserDetails(name, phone, email)
                        model.pushUserDetails(user)
                        findNavController().navigate(R.id.action_truckRegistration_to_customerMainFragment)
                    }
                }

            }
        }

    }

    private fun checkFor() {
        val check = navArgs.case
        val sharedPreferences = requireActivity().getSharedPreferences("check", Context.MODE_PRIVATE)
        val key = sharedPreferences.getString("value", "default")
        auth = FirebaseAuth.getInstance()
        if(check==10) {
            if(auth.uid!=null) {
                if(key=="1") {
                    findNavController().navigate(R.id.action_truckRegistration_to_truckMainFragment)
                }
                else {
                    auth.signOut()
                }
            }
            else {

            }
        }
        else {
            if(auth.uid!=null) {
                if(key=="0") {
                    findNavController().navigate(R.id.action_truckRegistration_to_customerMainFragment)
                }
                else {
                    requireActivity().stopService(Intent(requireContext(), TrackingService::class.java))
                    auth.signOut()
                    binding.cL1.visibility = View.GONE
                    binding.cL2.visibility = View.VISIBLE
                }
            }
            else {
                requireActivity().stopService(Intent(requireContext(), TrackingService::class.java))
                binding.cL1.visibility = View.GONE
                binding.cL2.visibility = View.VISIBLE
            }
        }
    }


    private fun setUpDialog() {
        dialog = ProgressDialog(requireContext())
        dialog.setTitle("Please wait..")
        dialog.setCancelable(false)
    }

    object Dialog {
         fun show() {
            dialog.show()
        }
         fun dismiss() {
            dialog.dismiss()
        }
    }


}