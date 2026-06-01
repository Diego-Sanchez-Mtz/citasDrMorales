package com.example.citasdrmorales.home.apponitments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.citasdrmorales.core.model.Appointment
import com.example.citasdrmorales.databinding.ItemAppointmentBinding

class AppointmentsAdapter(
    private val onItemClick: (Appointment) -> Unit = {}
): ListAdapter<Appointment, AppointmentsAdapter.AppointmentViewHolder>(DIFF){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AppointmentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class AppointmentViewHolder(
        private val binding: ItemAppointmentBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(appointment: Appointment){
            binding.tvDateTime.text = appointment.fecha_hora
            binding.tvDoctorName.text = appointment.doctor.nombre_completo
            binding.tvSpecialty.text = appointment.doctor.especialidad
            binding.tvStatus.text = appointment.estado

            binding.root.setOnClickListener {
                onItemClick(appointment)
            }

        }
    }
    companion object{
        private val DIFF = object: DiffUtil.ItemCallback<Appointment>(){
            override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment) =
                oldItem.id_cita == newItem.id_cita

            override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment) =
                oldItem == newItem

        }
    }
}