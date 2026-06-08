package com.example.citasdrmorales.home.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.citasdrmorales.R
import com.example.citasdrmorales.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Locale

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        setupListeners()
        observeViewModel()

        viewModel.loadUserData()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            // 1. Ocultamos la barra inferior usando su ID real de tu activity_home.xml
            activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNavigationView
            )?.visibility = View.GONE

            // 2. Destruimos la sesión del usuario en Firebase Auth
            viewModel.logout()

            // 3. LA SOLUCIÓN MAESTRA: Creamos un Intent para abrir la MainActivity (Login)
            // Pero le añadimos flags (banderas) de limpieza absoluta
            val intent = Intent(requireContext(), com.example.citasdrmorales.onboarding.MainActivity::class.java).apply {
                // Borra absolutamente todas las actividades que existan en la pila de memoria
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            // 4. Arrancamos el Login fresco y matamos la pantalla vieja
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Escuchar el correo electrónico
                launch {
                    viewModel.userEmail.collect { email ->
                        binding.tvEmailValue.text = email
                        if (email.isNotEmpty()) {
                            binding.tvAvatarLetter.text = email.take(1).uppercase(Locale.getDefault())
                        }
                    }
                }
                // Escuchar el contador de citas en tiempo real
                launch {
                    viewModel.appointmentsCount.collect { count ->
                        binding.tvTotalAppointments.text = count.toString()
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