package com.example.citasdrmorales.home.doctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citasdrmorales.core.FragmentCommunicator
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.databinding.FragmentDoctorsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DoctorsFragment : Fragment() {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DoctorsViewModel>()
    private lateinit var communicator: FragmentCommunicator
    private val adapter = DoctorsAdapter { appointment ->

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDoctors.adapter = adapter
        observeState()
        viewModel.loadDoctors()
        return binding.root
    }

    fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.doctorsState.collect { state ->
                    when (state){
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            adapter.submitList(state.data)
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