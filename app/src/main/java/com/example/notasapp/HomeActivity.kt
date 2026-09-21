package com.example.notasapp

import android.os.Bundle
import android.util.Log.v
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notasapp.databinding.HomeListNotesBinding
import com.example.notasapp.ui.theme.NotasAppTheme

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeListNotesBinding

    // cria o "Lançador" que sabe esperar um resultado
    private val addNoteLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // Esse bloco só roda quando voltamos da Tela 2. Verificamos se foi SUCESSO (RESULT_OK)
        if (result.resultCode == RESULT_OK) {
            // Como sabemos que a nota foi pro topo (posição 0), avisamos o adapter:
            binding.recyclerViewNotas.adapter?.notifyItemInserted(0)

            // Forçamos a lista a rolar pro topo para o usuário ver a nota nova
            binding.recyclerViewNotas.scrollToPosition(0)

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = HomeListNotesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // esconde o barra azul que é criada
        supportActionBar?.hide()
        // faz o app reconhecer a statusBar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            // Pega o tamanho exato da status e da  (nav bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Aplica esse tamanho como um padding na sua tela principal
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // instância do Adapter passando os Mock Datas
        val adapter = NotasAdapter(MockData.listaNotas)
        // avisa ao RecyclerView quem é o Adapter dele
        binding.recyclerViewNotas.adapter = adapter
        // fala como a lista deve se comportar (Vertical padrão)
        binding.recyclerViewNotas.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)


        binding.bttAddNote.setOnClickListener {

            val intent = android.content.Intent(this, NoteDetailActivity::class.java)

            addNoteLauncher.launch(intent)

        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewNotas.adapter?.notifyDataSetChanged()
    }


}


data class HomeListNota(
    val id: String,
    val title: String,
    val preview: String,
    val date: String,
    val address: String
)