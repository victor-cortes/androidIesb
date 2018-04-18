package com.josakin.supermercadojosakin

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.josakin.supermercadojosakin.dao.AppDatabase
import com.josakin.supermercadojosakin.entidades.Produto


class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    private var listagemAdapter: ProdutoListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()


        var produtos = db?.produtoDao()?.findAll()

        val listView = findViewById<ListView>(R.id.listViewProduto)
        listagemAdapter = ProdutoListAdapter(this, produtos!!)
        listView.adapter = listagemAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener {adapterView, view, position, id ->
            val myIntent = Intent(this, DetalharActivity::class.java)
            myIntent.putExtra("itemSelecionado", adapterView.getItemAtPosition(position) as Produto)
            startActivity(myIntent)
        }

        val buttonCadastrar = findViewById<Button>(R.id.buttonCadastrar)
        buttonCadastrar.setOnClickListener(View.OnClickListener {
            val myIntent = Intent(this, CadastrarActivity::class.java)
            startActivity(myIntent)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRestart() {
        super.onRestart()
        this.listagemAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        this.listagemAdapter?.setMlistProduto(db?.produtoDao()?.findAll()!!)
        this.listagemAdapter?.notifyDataSetChanged()

    }

}

private class ProdutoListAdapter(context: Context, listProduto: List<Produto>) : BaseAdapter()  {

    private val mContext: Context
    private var mListProduto: List<Produto>

    init {
        mContext = context
        mListProduto = listProduto
    }

    override fun getItem(position: Int): Any {
        return mListProduto.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mListProduto.size
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.row_produto,viewGroup,false)


        val nametextView = rowMain.findViewById<TextView>(R.id.textViewName)
        nametextView.text = mListProduto.get(position).name

        val descricaotextView = rowMain.findViewById<TextView>(R.id.textViewDescricao)
        val valor = mListProduto.get(position).valor.toString()
        descricaotextView.text = "R$  $valor"

        val imageViewProduto = rowMain.findViewById<ImageView>(R.id.imageViewProduto)


//        var bytes = android.util.Base64.decode(mListProduto.get(position).foto, android.util.Base64.DEFAULT)
//        imageViewProduto.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0, bytes.size))
        imageViewProduto.setImageBitmap(mListProduto.get(position).retornaBitMapImage())

        return rowMain
    }

    fun setMlistProduto(produtos : List<Produto>) {
        this.mListProduto = produtos
    }
    fun getMlistProduto() : List<Produto> {
        return this.mListProduto
    }

}
