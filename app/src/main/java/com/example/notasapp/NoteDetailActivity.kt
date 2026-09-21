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
        val tituloRecebido = intent.getStringExtra("EXTRA_TITULO")
        val ConteudoRecebido = intent.getStringExtra("EXTRA_CONTEUDO")
        val dataRecebida = intent.getStringExtra("EXTRA_DATA")
        val enderecoRecebido = intent.getStringExtra("EXTRA_ENDERECO")

        if (tituloRecebido != null) {
            binding.editTitle.setText(tituloRecebido)
            binding.editContent.setText(ConteudoRecebido)
            binding.editAddress.setText(enderecoRecebido)

            // Se tinha uma data salva, atualizamos o texto do botão e a nossa variável
            if (dataRecebida != null && dataRecebida != "") {
                binding.btnPickDate.text = dataRecebida
                dataSelecionada = dataRecebida // Mantém a memória da data para o botão de salvar
            }
        }

        binding.btnBack.setOnClickListener {

            // Verifica se é uma nota nova (se abriu pelo '+')
            if (tituloRecebido == null) {
                val novoTitulo = binding.editTitle.text.toString()
                val novoConteudo = binding.editContent.text.toString()

                // Só salva se a pessoa digitou alguma coisa!
                if (novoTitulo.isNotEmpty() || novoConteudo.isNotEmpty()) {
                    val notaNova = HomeListNota(
                        id = System.currentTimeMillis().toString(),
                        title = novoTitulo,
                        preview = novoConteudo,
                        date = dataSelecionada.ifEmpty { "Sem lembrete" }, // Usa a data do calendário!
                        address = binding.editAddress.text.toString()      // Pega o endereço da tela!
                    )
                    MockData.listaNotas.add(0, notaNova)
                    setResult(RESULT_OK) // Manda a Home atualizar
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