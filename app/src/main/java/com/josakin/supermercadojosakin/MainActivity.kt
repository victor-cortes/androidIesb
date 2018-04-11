package com.josakin.supermercadojosakin

import android.arch.persistence.room.Room
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import com.josakin.supermercadojosakin.dao.AppDatabase
import com.josakin.supermercadojosakin.entidades.Produto

class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()

        cadastraProduto()
        cadastraProduto()


        val produtos = db?.produtoDao()?.findAll()

        val constraintLayout = findViewById<ConstraintLayout>(R.id.cl)
        val listView = ListView(this)
        val adapter = ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, produtos)
        listView.adapter = adapter

        constraintLayout.addView(listView)
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

class ButtonListAdapter(
        private val context: Context,
        private val rowItens: List<Produto>
) : BaseAdapter() {
    override fun getItem(p0: Int): Any {
        return rowItens[p0]
    }

    override fun getItemId(p0: Int): Long {
        return rowItens.indexOf(getItemId(p0) as Produto).toLong()
    }

    override fun getCount(): Int {
        return rowItens.size
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
