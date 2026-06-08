package com.example.citasdrmorales.home.appointments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.citasdrmorales.R
import com.example.citasdrmorales.core.FragmentCommunicator
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.databinding.FragmentAppointmentsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AppointmentsFragment : Fragment() {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AppointmentsViewModel>()
    private val adapter = AppointmentsAdapter()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        setupRecyclerView()
        setupListeners()
        observeState()

        viewModel.loadAppointments()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = adapter
    }

    private fun setupListeners() {
        // ACCIÓN DEL BOTÓN VACÍO: Te manda al fragmento para registrar una cita usando Navigation Component
        binding.btnNavigateToCreate.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentsFragment_to_scheduleFragment)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appointmentState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            val appointments = state.data

                            android.widget.Toast.makeText(requireContext(), "Citas recibidas: ${appointments.size}", android.widget.Toast.LENGTH_SHORT).show()

                            if (appointments.isEmpty()) {
                                // SI NO HAY CITAS: Muestra el aviso y oculta la lista
                                binding.rvAppointments.isVisible = false
                                binding.clEmptyState.isVisible = true
                            } else {
                                // SI HAY CITAS: Muestra la lista, oculta el aviso y pinta los datos
                                binding.rvAppointments.isVisible = true
                                binding.clEmptyState.isVisible = false
                                adapter.submitList(appointments)
                            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}