package com.example.citasdrmorales.home.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.citasdrmorales.core.FragmentCommunicator
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.databinding.FragmentScheduleBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ScheduleViewModel>()
    private lateinit var communicator: FragmentCommunicator

    // Variables para almacenar temporalmente la fecha y hora seleccionadas
    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        viewModel.loadDoctorsForSchedule()

        setupSpinners()
        setupDateTimePickers()
        setupListeners()
        observeState()

        return binding.root
    }

    private fun setupSpinners() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.specialties.collect { listSpecialties ->
                    if (listSpecialties.isNotEmpty()) {
                        val listaConDefecto = listOf("Seleccionar Especialidad") + listSpecialties

                        val specialtyAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            listaConDefecto
                        )
                        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerEspecialidad.adapter = specialtyAdapter
                    }
                }
            }
        }

        binding.spinnerEspecialidad.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpecialty = binding.spinnerEspecialidad.selectedItem.toString()
                viewModel.filterDoctorsBySpecialty(selectedSpecialty)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.filteredDoctors.collect { doctorsList ->
                    val doctorNames =
                        listOf("Seleccionar Médico") + doctorsList.map { it.nombreCompleto }

                    val doctorAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        doctorNames
                    )
                    doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerDoctor.adapter = doctorAdapter
                }
            }
        }
    }

    private fun setupDateTimePickers() {
        val calendar = Calendar.getInstance()

        // Configurar el botón de selección de Fecha (Calendario)
        binding.btnDatePicker.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Guardamos la fecha en un formato limpio (Mes + 1 porque en Java/Kotlin los meses van de 0 a 11)
                selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year)
                updateDateTimeText()
            }

            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Configurar el botón de selección de Hora (Reloj)
        binding.btnTimePicker.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                updateDateTimeText()
            }

            TimePickerDialog(
                requireContext(),
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // Formato de 24 horas
            ).show()
        }
    }

    private fun updateDateTimeText() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            binding.tvSelectedDateTime.text = "Programada para el: $selectedDate a las $selectedTime hrs."
        } else if (selectedDate.isNotEmpty()) {
            binding.tvSelectedDateTime.text = "Fecha: $selectedDate (Falta seleccionar hora)"
        } else if (selectedTime.isNotEmpty()) {
            binding.tvSelectedDateTime.text = "Hora: $selectedTime hrs. (Falta seleccionar fecha)"
        }
    }

    private fun setupListeners() {
        // Botón final para confirmar y agendar la cita
        binding.btnConfirmAppointment.setOnClickListener {
            val specialty = binding.spinnerEspecialidad.selectedItem.toString()
            val doctor = binding.spinnerDoctor.selectedItem.toString()
            val dateTime = binding.tvSelectedDateTime.text.toString()

            viewModel.saveAppointment(specialty, doctor, dateTime)
        }
        binding.btnCancelSchedule.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            Toast.makeText(requireContext(), state.data, Toast.LENGTH_LONG).show()

                            // Limpiamos el estado y regresamos automáticamente al Home
                            viewModel.resetState()
                            findNavController().popBackStack()
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                        null -> {}
                    }
                }

            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loadingState.collect { isLoading ->
                        communicator.manageLoader(isLoading)
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