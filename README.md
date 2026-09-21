# NotasApp (Remember_it)

## Objetivo do Aplicativo
O **NotasApp** é um aplicativo de anotações inspirado na experiência de usuário do Apple Notes. Seu principal objetivo é permitir que os usuários criem, visualizem e excluam anotações de forma rápida e intuitiva. Além do conteúdo de texto, o aplicativo permite associar datas de lembretes e endereços às notas. Como diferencial de interatividade, as notas podem ser deletadas através de um gesto nativo de arrastar (Swipe-to-Delete) e o endereço cadastrado abre automaticamente o Google Maps com um clique longo.

---

## Como rodar o projeto localmente

O projeto foi construído para ser executado de forma imediata (plug-and-play), sem a necessidade de configurações complexas.

**Passo a passo:**
1. Clone este repositório em sua máquina:
   ```bash
   git clone https://github.com/SavioDec/Remember_it.git
   ```
2. Abra o **Android Studio**.
3. Selecione a opção **File > Open** e navegue até a pasta do repositório clonado.
4. Aguarde o Android Studio realizar a sincronização do Gradle (Sync).
5. Selecione um emulador (API 24 ou superior recomendada) ou conecte um dispositivo físico via depuração USB.
6. Clique no botão de **Run (Shift + F10)**.

> **Aviso sobre Chaves e APIs:** Este aplicativo **não** utiliza APIs externas, Firebase ou banco de dados nesta etapa. Todo o sistema de dados é baseado em Mocks em memória. Portanto, **nenhum arquivo `.env` ou chave de API é necessário** para compilar e executar o projeto.

---

## Bibliotecas Externas Utilizadas

Este projeto foi desenvolvido utilizando exclusivamente as bibliotecas nativas e oficiais do ecossistema Android. **Nenhuma biblioteca externa de terceiros (como Retrofit, Glide, etc.) foi incluída nesta etapa.**

Bibliotecas nativas destacadas no projeto:
* **AndroidX Core & AppCompat:** Base de compatibilidade do projeto.
* **Material Design Components:** Para a estilização visual (ex: `FloatingActionButton`).
* **ViewBinding:** Ferramenta oficial do Google para interagir com Views de forma segura (type-safe).
* **RecyclerView & ItemTouchHelper:** Para a renderização e interatividade da lista de notas.

---

## Atendimento aos Requisitos do Projeto

Para facilitar a correção e avaliação, abaixo estão mapeados os requisitos da atividade e como foram implementados na arquitetura do aplicativo:

### Requisitos Obrigatórios:
1. **Duas telas em Android Views com layouts XML:** 
   * A aplicação possui duas Activities: `HomeActivity` e `NoteDetailActivity`. Seus respectivos layouts (`home_list_notes.xml` e `activity_note_detail.xml`) utilizam `FrameLayout`, `LinearLayout`, `RecyclerView`, `TextView` e `EditText`.
2. **Navegação com Intent Explícita e Passagem de Dados:**
   * A navegação da `HomeActivity` para a `NoteDetailActivity` ocorre via Intent Explícita. Ao clicar em um Card existente, dados como Título, Conteúdo, Data e Endereço são trafegados via `putExtra` e recebidos do outro lado. 
   * O fluxo reverso utiliza `registerForActivityResult`, trafegando o `RESULT_OK` de volta para a Home atualizar a lista perfeitamente.
3. **Conexão com ViewBinding e Interações:**
   * As views estão conectadas ao código Kotlin. Tratamos cliques comuns (`setOnClickListener`) e cliques longos (`setOnLongClickListener`).
4. **Modelos imutáveis (data class) e valores opcionais:**
   * Os dados são estritamente representados pela `data class HomeListNota` (val id, val title, etc).
   * Tratamento de valores nulos (opcionais) é realizado ao receber a Intent: se não houver dados, o app entende que é uma nota nova; se houver, a tela entra em modo de leitura.
5. **Utilização de Mocks:**
   * A persistência em tempo de execução foi arquitetada usando o padrão Singleton através do arquivo `MockData.kt`. Ele contém uma `MutableList` de anotações que simula o comportamento de um banco de dados real.

### Itens Opcionais Atendidos:
* ✅ **Utilizar apenas ViewBinding ao invés de findViewById:** Todo o código do projeto é livre de `findViewById`. A comunicação com a UI é 100% feita via instâncias de Binding (`ActivityNoteDetailBinding`, `HomeListNotesBinding` e `ItemNotaBinding`).

### Funcionalidade Extra (Destaque Técnico):
* **Swipe-to-Delete interativo:** O aplicativo intercepta gestos na `RecyclerView` (usando `ItemTouchHelper` e a classe `Canvas`). Ao arrastar um card, sua transparência (alpha) é alterada e um background desenhado com a lixeira é revelado antes do item ser deletado do Mock.
* **Intent Implícita (Maps):** Um clique longo no endereço da nota intercepta o roteamento padrão do Android abrindo o Maps.
