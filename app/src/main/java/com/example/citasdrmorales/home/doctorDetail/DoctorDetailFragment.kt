package com.example.citasdrmorales.home.doctorDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.citasdrmorales.databinding.FragmentDoctorDetailBinding

class DoctorDetailFragment : Fragment() {

    // 1. Configuramos View Binding de forma segura para evitar fugas de memoria
    private var _binding: FragmentDoctorDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. RECUPERAMOS LOS DATOS: Extraemos los textos del Bundle usando las mismas llaves (keys)
        val nombre = arguments?.getString("nombre") ?: "Médico no especificado"
        val especialidad = arguments?.getString("especialidad") ?: "General"
        val experiencia = arguments?.getString("experiencia") ?: "No especificada"
        val disponibilidad = arguments?.getString("disponibilidad") ?: "No disponible"
        val correo = arguments?.getString("correo") ?: "Sin correo de contacto"
        val universidad = arguments?.getString("universidad") ?: "No especificada"

        binding.btnBackToDoctors.setOnClickListener {
            requireView().findNavController().popBackStack()
        }

        // 3. PINTAMOS EN PANTALLA: Asignamos los textos a los TextViews de tu fragment_doctor_detail.xml
        binding.tvDetailName.text = nombre
        binding.tvDetailSpecialty.text = especialidad
        binding.tvDetailExperience.text = experiencia
        binding.tvDetailEducation.text = universidad
        binding.tvDetailAvailability.text = disponibilidad
        binding.tvDetailEmail.text = correo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 4. Limpiamos el binding al destruir la vista (buena práctica escolar obligatoria)
        _binding = null
    }
}