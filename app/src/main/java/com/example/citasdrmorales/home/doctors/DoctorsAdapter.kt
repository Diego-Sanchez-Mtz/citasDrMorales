package com.example.citasdrmorales.home.doctors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.citasdrmorales.core.model.DoctorModel
import com.example.citasdrmorales.databinding.ItemDoctorBinding

class DoctorsAdapter(
    private val onItemClick: (DoctorModel) -> Unit = {}
): ListAdapter<DoctorModel, DoctorsAdapter.DoctorViewHolder>(DIFF){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DoctorViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class DoctorViewHolder(
        private val binding: ItemDoctorBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(doctor: DoctorModel){
            binding.tvNombreDoctor.text = doctor.nombreCompleto
            binding.tvEspecialidad.text = doctor.especialidad
            binding.tvExperiencia.text = doctor.experiencia
            binding.tvDisponibilidad.text = doctor.disponibilidad

            binding.root.setOnClickListener {
                onItemClick(doctor)
            }

        }
    }
    companion object{
        private val DIFF = object: DiffUtil.ItemCallback<DoctorModel>(){
            override fun areItemsTheSame(oldItem: DoctorModel, newItem: DoctorModel) =
                oldItem.nombreCompleto == newItem.nombreCompleto

            override fun areContentsTheSame(oldItem: DoctorModel, newItem: DoctorModel) =
                oldItem == newItem

        }
    }
}