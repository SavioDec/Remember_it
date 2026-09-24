package com.example.notasapp

import android.graphics.Canvas
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
import androidx.recyclerview.widget.RecyclerView
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


        val regraDeArrasto = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback (
            0, // nao permite arrastar para cima/baixo
            androidx.recyclerview.widget.ItemTouchHelper.LEFT //permite arrastar apenas para a esquerda
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicao = viewHolder.adapterPosition

                MockData.listaNotas.removeAt(posicao)

                adapter.notifyItemRemoved(posicao)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                val alpha = 1.0f - Math.abs(dX) / itemView.width.toFloat()
                itemView.alpha = alpha

                val paint = android.graphics.Paint()
                paint.color = android.graphics.Color.parseColor("#FF4444")

                itemView.right.toFloat() + dX
                val background = android.graphics.RectF(
                    itemView.right.toFloat() + dX, // A borda esquerda do fundo acompanha o card
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                c.drawRect(background, paint)
                val icone = androidx.core.content.ContextCompat.getDrawable(this@HomeActivity, android.R.drawable.ic_menu_delete)
                if (icone != null){
                    val iconMargin = (itemHeight - icone.intrinsicHeight) / 2
                    val iconTop = itemView.top + iconMargin
                    val iconBottom = iconTop + icone.intrinsicHeight
                    val iconLeft = itemView.right - iconMargin - icone.intrinsicWidth
                    val iconRight = itemView.right - iconMargin

                    // Só desenha o ícone se o usuário arrastou o suficiente para ele aparecer
                    if (Math.abs(dX) > iconMargin) {
                        icone.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icone.draw(c)
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }

        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(regraDeArrasto)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewNotas)

    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewNotas.adapter?.notifyDataSetChanged()
    }


}


data class HomeListNota(
    val id: String,
    val title: String,
    val preview: String?,
    val date: String?,
    val address: String?
)