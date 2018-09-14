package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Noticia
import java.io.ByteArrayOutputStream
import java.util.*

class HomeActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    val TEXTO = "Em julho de 2011 a JetBrains revelou o Projeto Kotlin, no qual já estava trabalhando há um ano. Dmitry Jemerov disse que a maioria das linguagens não possuiam as características que eles da JetBrains estavam procurando, com exceção da linguagem Scala, no entanto, Dmitry Jemerov citou que o tempo de compilação lenta do Scala era uma deficiência óbvia. Um dos objetivos declarados da Kotlin é compilar tão rápido quanto Java."

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()


        var noticias = cadastraNoticia()

        val listRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        listRecyclerView.itemAnimator = DefaultItemAnimator()
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = NoticiaListAdapter(this, noticias!!)
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editarPerfil -> {
                val myIntent = Intent(this, EditarPerfilActivity::class.java)
                startActivity(myIntent)
                true
            }
            R.id.sair -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastraNoticia(): List<Noticia>? {
        var noticias = db?.noticiaDao()?.findAll()

        if (noticias?.size == 0) {

            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo1", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo2", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo3", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo4", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo5", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo6", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo7", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo8", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo9", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo10", Date(), TEXTO, getImagem()))

            noticias = db?.noticiaDao()?.findAll()
        }

        return noticias?.sortedByDescending { it.uid }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getImagem(): String {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.noticia_logo)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
        bitmap.recycle()
        return base64
    }

}

class NoticiaListAdapter(paramContexto: Context, paramNoticias: List<Noticia>) : RecyclerView.Adapter<NoticiaViewHolder>() {

    private val contexto: Context
    private var noticias: List<Noticia>

    init {
        contexto = paramContexto
        noticias = paramNoticias
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val contentHomeitemView = LayoutInflater.from(this.contexto).inflate(R.layout.content_home, parent, false)
        return NoticiaViewHolder(contentHomeitemView)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {

        val item = this.noticias[position]
        holder.textViewTituloNoticia.text = item.titulo
        holder.textViewTextoNoticia.text = item.texto
        holder.textViewDataNoticia.text = item.getDataString()
        holder.imageViewImagemNoticia.setImageBitmap(item.retornaBitMapImage())

        holder.bind(noticias[position], itemOnClick)
    }

    private val itemOnClick: (Noticia) -> Unit = { noticia ->
        val intent = Intent(this.contexto, DetalhaNoticiaActivity::class.java)
        intent.putExtra("itemSelecionado", noticia)
        this.contexto.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return this.noticias.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}

class NoticiaViewHolder(contentHomeitemView: View) : RecyclerView.ViewHolder(contentHomeitemView) {

    var textViewTituloNoticia: TextView
    var textViewTextoNoticia: TextView
    var textViewDataNoticia: TextView
    var imageViewImagemNoticia: ImageView
    var contentView:View

    init{

        textViewTituloNoticia = contentHomeitemView.findViewById(R.id.textViewTituloNoticia)
        textViewTextoNoticia = contentHomeitemView.findViewById(R.id.textViewTextoNoticia)
        textViewDataNoticia = contentHomeitemView.findViewById(R.id.textViewDataNoticia)
        imageViewImagemNoticia = contentHomeitemView.findViewById(R.id.imageViewImagemNoticia)
        contentView = contentHomeitemView
    }

    fun bind(noticia: Noticia, clickListener: (Noticia) -> Unit) {
        contentView.setOnClickListener { clickListener(noticia)}
    }
}