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


private val homeList = List(3){
    if (it == 0){
        return@List HomeListNota(
            it.toString(),
            "Minha Primeira Nota",
            "um conteudo qualquer",
            "20/09/2026"
        )
    }
    HomeListNota(it.toString(), "Title $it", "Seila, Qualquer coisa", "20/09/2026")
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeListNotesBinding

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

        // 1. instância do Adapter passando os Mock Datas
        val adapter = NotasAdapter(homeList)
        // 2. avisa ao RecyclerView quem é o Adapter dele
        binding.recyclerViewNotas.adapter = adapter
        // 3. fala como a lista deve se comportar (Vertical padrão)
        binding.recyclerViewNotas.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)


    }


}


data class HomeListNota(
    val id: String,
    val title: String,
    val preview: String,
    val date: String
)