package mobile.iesb.br.projetofinal.activitys

import android.Manifest
import android.app.AlertDialog
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.content_editar_perfil.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ValidaUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class EditarPerfilActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    val CAMERA = 0
    val GALERIA = 1

    val GALLERY_REQUEST_CODE = 1
    val CAMERA_REQUEST_CODE = 2

    private var mAuth: FirebaseAuth? = null
    private var currentUser:FirebaseUser? = null
    private var key:String? = null

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        this.currentUser = mAuth?.getCurrentUser()
//        updateUI(currentUser)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        this.mAuth = FirebaseAuth.getInstance();

        imageViewEdicao.setOnClickListener {
            showPictureDialog()
        }

        backButton.setOnClickListener {
            finish()
        }

        var usuarioRef = FirebaseDatabase.getInstance().getReference()
        textViewEmailUsuario.text = this.mAuth!!.currentUser!!.email!!

        usuarioRef.child("usuarios").orderByChild("email").equalTo(mAuth!!.currentUser!!.email).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){

                for(usuario:DataSnapshot in dataSnapshot.children) {
                        editTextNomeUsuario.text = Editable.Factory.getInstance().newEditable(usuario.child("nome").value.toString())
                        editTextMatricula.text = Editable.Factory.getInstance().newEditable(usuario.child("matricula").value.toString())
                        editTextTelefone.text = Editable.Factory.getInstance().newEditable(usuario.child("telefone").value.toString())


                    key = usuario.key

//                    carregarImagemPadrao()
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })

//        imageViewEdicao.setImageBitmap(usuario?.retornaBitMapImage()!!)

//        editTextNomeUsuario.setText(usuario?.nome)
//        editTextMatricula.setText(usuario?.matricula.toString())
//        editTextTelefone.setText(usuario?.telefone.toString())

        buttonAlterar.setOnClickListener {
            alteraUsuario()
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Selecione")
        val pictureDialogItems = arrayOf("Camera", "Galeria")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                CAMERA -> takePhotoFromCamera()
                GALERIA -> choosePhotoFromGallary()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_REQUEST_CODE)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    private fun takePhotoFromCamera() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun makeRequest(permissao: String, codigo: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissao), codigo)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Permissão não concedida!", Toast.LENGTH_SHORT).show()
                }
                return
            }
            GALLERY_REQUEST_CODE -> {
                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Permissão não concedida!", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun alteraUsuario() {
        var usuario = Usuario()
        var senhaAlterada = true
        var possuiError = false
        if (editTextSenhaCadastro.text.isEmpty() && editTextConfirmaSenhaCadastro.text.isEmpty()) {
            senhaAlterada = false
        }

        if (senhaAlterada) {
            if (ValidaUtil.isPasswordValido(editTextSenhaCadastro) && ValidaUtil.isPasswordValido(editTextConfirmaSenhaCadastro)) {
                if (!editTextSenhaCadastro.text.toString().equals(editTextConfirmaSenhaCadastro.text.toString())) {
                    Toast.makeText(applicationContext, "As senhas não conferem.", Toast.LENGTH_LONG).show()
                    possuiError = true
                    return
                } else {
                    usuario?.senha = editTextSenhaCadastro.text.toString()
                    mAuth = FirebaseAuth.getInstance()
                    mAuth!!.currentUser!!.updatePassword(usuario?.senha)
                }
            }
        }

        if(!possuiError) {

            usuario?.nome = editTextNomeUsuario.text.toString()
            usuario?.email = textViewEmailUsuario.text.toString()
//        usuario?.foto = imageToBase64(imageViewEdicao)
            usuario?.matricula = editTextMatricula.text.toString().toLong()
            usuario?.telefone = editTextTelefone.text.toString().toLong()


            if(key == null) {
                key = UUID.randomUUID().toString()
            }

            val usuarioRef = FirebaseDatabase.getInstance().getReference("/usuarios/${key}")
            usuarioRef.setValue(usuario)

            Toast.makeText(this, "Usuário alterado com Sucesso", Toast.LENGTH_SHORT).show()

        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    imageViewEdicao!!.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI))
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erro ao recuperar imagem!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            imageViewEdicao!!.setImageBitmap(data!!.extras!!.get("data") as Bitmap)
        }
    }

    fun abrirCamera(abreCamera: Intent) {
        startActivityForResult(abreCamera, CAMERA_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun imageToBase64(image: ImageView): String {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)

        bitmap.recycle()
        return base64
    }

}
