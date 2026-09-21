package com.example.notasapp

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.notasapp.databinding.ItemNotaBinding
import androidx.core.net.toUri

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
        holder.binding.textAddress.text = notaAtual.address

        holder.binding.textAddress.setOnLongClickListener {
            if (notaAtual.address.isNotEmpty()) {
                val uri = "geo:0,0?q=${android.net.Uri.encode(notaAtual.address)}".toUri()
                val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)

                // O adapter precisa do 'context' da tela para poder navegar
                it.context.startActivity(mapIntent)
            }
            true // Consome o clique longo
        }


        // Quando alguém clicar no card
        holder.binding.root.setOnClickListener {

            // Pegamos o 'context' (a tela onde estamos) para poder navegar
            val contexto = holder.binding.root.context
            val intent = android.content.Intent(contexto, NoteDetailActivity::class.java)

            intent.putExtra("EXTRA_TITULO", notaAtual.title)
            intent.putExtra("EXTRA_CONTEUDO", notaAtual.preview)
            intent.putExtra("EXTRA_DATA", notaAtual.date)
            intent.putExtra("EXTRA_ENDERECO", notaAtual.address)

            contexto.startActivity(intent)

        }


    }
}