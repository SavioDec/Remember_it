package com.example.notasapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notasapp.databinding.ActivityNoteDetailBinding
import androidx.core.net.toUri


class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // esconde o barra azul que é criada
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        var dataSelecionada = ""
        val id = intent.getStringExtra("EXTRA_NOTA_ID")
        val note = MockData.listaNotas.find { it.id == id }

        if (note != null) {

            binding.editTitle.setText(note.title)
            binding.editContent.setText(note.preview)
            binding.editAddress.setText(note.address)

            // Se tinha uma data salva, atualizamos o texto do botão e a nossa variável
            if (note.date?.isNotEmpty() == true) {
                binding.btnPickDate.text = note.date
                dataSelecionada = note.date // Mantém a memória da data para o botão de salvar
            }
        }

        binding.btnBack.setOnClickListener {

            if (binding.editTitle.text.isEmpty()){
                finish()
                return@setOnClickListener
            }

            // Verifica se é uma nota nova (se abriu pelo '+')
            if (note == null) {
                val novoTitulo = binding.editTitle.text.toString()
                val novoConteudo = binding.editContent.text.toString()

                // Só salva se a pessoa digitou alguma coisa!
                if (novoTitulo.isNotEmpty() || novoConteudo.isNotEmpty()) {
                    val notaNova = HomeListNota(
                        id = System.currentTimeMillis().toString(),
                        title = novoTitulo,
                        preview = novoConteudo.ifEmpty { null },
                        date = dataSelecionada.ifEmpty { null }, // Usa a data do calendário!
                        address = binding.editAddress.text.toString().ifEmpty { null }     // Pega o endereço da tela!
                    )
                    MockData.listaNotas.add(0, notaNova)
                    setResult(RESULT_OK) // Manda a Home atualizar
                }
            }else {
                val index = MockData.listaNotas.indexOfFirst { it.id == note.id }
                if (index != -1) {
                    val novoTitulo = binding.editTitle.text.toString()
                    val novoConteudo = binding.editContent.text.toString()

                    MockData.listaNotas[index] = MockData.listaNotas[index].copy(
                        title = novoTitulo,
                        preview = novoConteudo.ifEmpty { null },
                        date = binding.btnPickDate.text.toString().ifEmpty { null },
                        address = binding.editAddress.text.toString().ifEmpty { null }
                    )
                }
            }
            finish()
        }



        binding.btnPickDate.setOnClickListener {

            val calendario = java.util.Calendar.getInstance()

            val datePickerDialog = android.app.DatePickerDialog(
                this, { _, ano, mes, dia ->
                    dataSelecionada = "$dia/${mes + 1}/$ano"

                    binding.btnPickDate.text = dataSelecionada
                },
                calendario.get(java.util.Calendar.YEAR),
                calendario.get(java.util.Calendar.MONTH),
                calendario.get(java.util.Calendar.DAY_OF_MONTH)


            )
            datePickerDialog.show()
        }

        binding.editAddress.setOnLongClickListener {
            val enderecoDigitado = binding.editAddress.text.toString()

            if (enderecoDigitado.isNotEmpty()) {
                // Monta a URL de pesquisa do Google Maps (geo:0,0?q=ENDEREÇO)
                val uri = "geo:0,0?q=${android.net.Uri.encode(enderecoDigitado)}".toUri()
                val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)

                // Dispara o aplicativo de mapas
                startActivity(mapIntent)
            }
            true // Retorna true para avisar que o clique longo foi consumido
        }


    }


}