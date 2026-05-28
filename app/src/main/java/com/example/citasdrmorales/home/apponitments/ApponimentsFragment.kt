package com.example.citasdrmorales.home.apponitments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.citasdrmorales.R
import com.example.citasdrmorales.core.FragmentCommunicator
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.databinding.FragmentApponimentsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ApponimentsFragment : Fragment() {

    private var _binding: FragmentApponimentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AppointmentsViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentApponimentsBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        observeState()
        viewModel.loadAppointments()
        return binding.root
    }

    fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.appointmentState.collect { state ->
                    when (state){
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            Log.i("Apponitments", "Song List: ${state.data}")
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> {}
                    }
                }
            }
        }
    }

}