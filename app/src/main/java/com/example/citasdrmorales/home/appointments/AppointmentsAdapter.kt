package com.example.citasdrmorales.home.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.citasdrmorales.core.model.AppointmentFirebase
import com.example.citasdrmorales.databinding.ItemAppointmentBinding

class AppointmentsAdapter : ListAdapter<AppointmentFirebase, AppointmentsAdapter.AppointmentViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppointmentViewHolder(private val binding: ItemAppointmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: AppointmentFirebase) {
            binding.tvDoctorName.text = appointment.doctor
            binding.tvSpecialty.text = appointment.especialidad
            binding.tvDateTime.text = appointment.fechaHora
            binding.tvSpecialty.text = appointment.estado
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<AppointmentFirebase>() {
            override fun areItemsTheSame(oldItem: AppointmentFirebase, newItem: AppointmentFirebase) = oldItem.idCita == newItem.idCita
            override fun areContentsTheSame(oldItem: AppointmentFirebase, newItem: AppointmentFirebase) = oldItem == newItem
        }
    }
}