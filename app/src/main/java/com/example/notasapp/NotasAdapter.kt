package com.example.notasapp

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.notasapp.databinding.ItemNotaBinding

class NotasAdapter(private val notas: List<HomeListNota>): RecyclerView.Adapter<NotasAdapter.NotaViewHolder>(){
    class NotaViewHolder ( val binding: ItemNotaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = ItemNotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotaViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val notaAtual = notas[position]

        holder.binding.textTitle.text = notaAtual.title
        holder.binding.textDate.text = notaAtual.date
        holder.binding.textPreview.text = notaAtual.preview
    }
}