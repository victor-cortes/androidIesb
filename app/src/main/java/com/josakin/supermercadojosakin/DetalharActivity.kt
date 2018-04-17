package com.josakin.supermercadojosakin

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.josakin.supermercadojosakin.dao.AppDatabase
import com.josakin.supermercadojosakin.entidades.Produto
import kotlinx.android.synthetic.main.activity_detalhar.*
import java.io.ByteArrayOutputStream

class DetalharActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var produtoSelecionado : Produto? = null
    val CAMERA_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhar)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()

        produtoSelecionado = intent.extras.get("itemSelecionado") as Produto


        val nomeProdutoText = findViewById<EditText>(nomeProduto.id)
        nomeProdutoText.setText(produtoSelecionado?.name)

        val valorProdutoText = findViewById<EditText>(valorProduto.id)
        valorProdutoText.setText(produtoSelecionado?.valor.toString())

        val imageView = findViewById<ImageView>(imageView.id)
        imageView.setImageBitmap(produtoSelecionado?.retornaBitMapImage())


        botaoAlterar.setOnClickListener {
            alterarProduto()
        }

        imageButton.setOnClickListener {
            val abreCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (abreCamera.resolveActivity(packageManager) != null) {
                startActivityForResult(abreCamera, CAMERA_REQUEST_CODE)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Comando não conhecido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun alterarProduto() {
        this.produtoSelecionado?.name = nomeProduto.text.toString()
        this.produtoSelecionado?.valor = valorProduto.text.toString().toDouble()
        this.produtoSelecionado?.foto = imageToBase64(imageView)

        db?.produtoDao()?.alteraProduto(this.produtoSelecionado!!)

        Toast.makeText(this, "Produto Alterado com Sucesso", Toast.LENGTH_SHORT).show()

        this.finish()

    }

    private fun imageToBase64(image: ImageView): String {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)

        bitmap.recycle()
        return base64
    }

}

