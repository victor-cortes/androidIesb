package com.josakin.supermercadojosakin

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.josakin.supermercadojosakin.dao.AppDatabase
import com.josakin.supermercadojosakin.entidades.Produto
import kotlinx.android.synthetic.main.activity_cadastrar.*
import java.io.ByteArrayOutputStream

class CadastrarActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    val CAMERA_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)


        imageButton.setOnClickListener{
            val abreCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(abreCamera.resolveActivity(packageManager) != null){
                startActivityForResult(abreCamera, CAMERA_REQUEST_CODE)
            }
        }


        botaoCadastrar.setOnClickListener{
            cadastraProduto()
        }

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
                }
            } else -> {
            Toast.makeText(this, "Comando não conhecido", Toast.LENGTH_SHORT).show()
        }
        }
    }

    fun cadastraProduto() {
        db?.produtoDao()?.insertProduto(Produto(name = nomeProduto.text.toString(), foto = imageToBitmap(imageView), valor = valorProduto.text.toString().toDouble()))
        Toast.makeText(this, "Produto Cadastrado com Sucesso", Toast.LENGTH_SHORT).show()
    }

    fun alteraProduto(produto: Produto) {
        db?.produtoDao()?.alteraProduto(produto)
    }

    fun excluirProduto(produto: Produto) {
        db?.produtoDao()?.removeProduto(produto)
    }

    private fun imageToBitmap(image: ImageView): String {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray().toString()
    }
}