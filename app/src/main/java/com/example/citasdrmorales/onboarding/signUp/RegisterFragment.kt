package com.example.citasdrmorales.onboarding.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.citasdrmorales.R
import androidx.navigation.fragment.findNavController
import com.example.citasdrmorales.core.FragmentCommunicator
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class RegisterFragment: Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() =_binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        setupValidation()
        setupClickListeners()
        observeState()
        return binding.root
    }

    private fun setupValidation() {
        binding.btnRegistrar.isEnabled = false
        val watcher = { validateAndEnable() }
        binding.tietEmailRegister.addTextChangedListener { validateAndEnable() }
        binding.tietPassRegister.addTextChangedListener { validateAndEnable() }
        binding.tietConfirmPassRegister.addTextChangedListener { validateAndEnable() }
    }

    private fun validateAndEnable() {
        val email = binding.tietEmailRegister.text.toString().trim()
        val pass = binding.tietPassRegister.text.toString().trim()
        val confirm = binding.tietConfirmPassRegister.text.toString().trim()

        binding.tilEmailRegister.error = viewModel.validateEmail(email)
        binding.tilPassRegister.error = viewModel.validatePassword(pass)
        binding.tilConfirmPassRegister.error =
            viewModel.validateConfirmPassword(pass, confirm)

        binding.btnRegistrar.isEnabled =
            viewModel.isRegisterFormValid(email, pass, confirm)
    }

    private fun setupClickListeners() {
        binding.btnRegistrar.setOnClickListener {
            val email = binding.tietEmailRegister.text.toString().trim()
            val password = binding.tietPassRegister.text.toString().trim()
            viewModel.requestSignUp(email, password)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                            binding.btnRegistrar.isEnabled = false
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            findNavController().navigate(R.id.action_fragment_register_to_personalInfoFragment)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.btnRegistrar.isEnabled = true
                            Snackbar.make(binding.root, state.error,
                                Snackbar.LENGTH_LONG).show()
                        }
                        null -> Unit
                    }
                }
            }
        }
    }
}