package com.example.trucktrackingsystem.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trucktrackingsystem.R
import com.example.trucktrackingsystem.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val navArgs: LoginFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        checkFor()
        if(auth.uid!=null) {
            auth.signOut()
        }
        binding.login.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val pass = binding.passwordEdt.text.toString()
            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Enter email and pass", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if(p0.isSuccessful) {
                            when(navArgs.key) {
                                10 -> {
                                    val sharedPreferences = requireActivity().getSharedPreferences("check", Context.MODE_PRIVATE)
                                    val edit = sharedPreferences.edit()
                                    edit.putString("value", "1").apply()
                                    findNavController().navigate(R.id.action_loginFragment_to_truckMainFragment)
                                }
                                20 ->  {
                                    val sharedPreferences = requireActivity().getSharedPreferences("check", Context.MODE_PRIVATE)
                                    val edit = sharedPreferences.edit()
                                    edit.putString("value", "0").apply()
                                    findNavController().navigate(R.id.action_loginFragment_to_customerMainFragment)
                                }
                            }
                        }
                    }

                })

            }
        }
    }

    private fun checkFor() {
        val key = navArgs.key
        if(key==20) {
            binding.genTextUsers.visibility = View.VISIBLE
            binding.genText.visibility = View.GONE
        }
    }
}