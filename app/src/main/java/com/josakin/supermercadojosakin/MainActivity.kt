package com.josakin.supermercadojosakin

import android.arch.persistence.room.Room
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.josakin.supermercadojosakin.dao.AppDatabase
import com.josakin.supermercadojosakin.entidades.Produto
import android.content.Intent



class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()


        var produtos = db?.produtoDao()?.findAll()

        if(produtos == null || produtos.size <= 0) {
            cadastraProduto()
            cadastraProduto()

            produtos = db?.produtoDao()?.findAll()
        }


        val listView = findViewById<ListView>(R.id.listViewProduto)
        listView.adapter = ProdutoListAdapter(this, produtos!!)


        val buttonCadastrar = findViewById<Button>(R.id.buttonCadastrar)
        buttonCadastrar.setOnClickListener(View.OnClickListener {
            val myIntent = Intent(this, CadastrarActivity::class.java)
            startActivity(myIntent)
        })

    }

    fun cadastraProduto() {
        db?.produtoDao()?.insertProduto(Produto(name = "S8", foto = "asdas", valor = 3000.0))
    }

    fun alteraProduto(produto: Produto) {
        db?.produtoDao()?.alteraProduto(produto)
    }

    fun excluirProduto(produto: Produto) {
        db?.produtoDao()?.removeProduto(produto)
    }
}

private class ProdutoListAdapter(context: Context, listProduto: List<Produto>) : BaseAdapter() {

    private val mContext: Context
    private val mListProduto: List<Produto>

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


        return rowMain
    }

}
