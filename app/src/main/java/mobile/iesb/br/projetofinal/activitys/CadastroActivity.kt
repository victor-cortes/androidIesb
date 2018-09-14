package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_cadastro.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ValidaUtil

import com.google.firebase.auth.FirebaseUser


import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.support.v4.app.FragmentActivity


class CadastroActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var mAuth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        this.mAuth = FirebaseAuth.getInstance()

        buttonCadastrar.setOnClickListener { view ->
            cadastraUsuario()
        }

//        db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java, "room-database"
//        ).allowMainThreadQueries().build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cadastraUsuario() {
        var email = findViewById<EditText>(R.id.editTextEmailCadastro)
        var senha = findViewById<EditText>(R.id.editTextSenhaCadastro)
        var senhaConfirmar = findViewById<EditText>(R.id.editTextConfirmaSenhaCadastro)

        if (ValidaUtil.isEmailValido(email) && ValidaUtil.isPasswordValido(senha) && ValidaUtil.isPasswordValido(senhaConfirmar)) {
            if (!senha.text.toString().equals(senhaConfirmar.text.toString())) {
                Toast.makeText(applicationContext, "As senhas não conferem.", Toast.LENGTH_LONG).show()
                return
            }



            var senhaString:String = senha?.text.toString()
            var emailString:String = email?.text.toString()
            mAuth!!.createUserWithEmailAndPassword(emailString, senhaString)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
//                            val user = mAuth!!.getCurrentUser()
                            Toast.makeText(applicationContext, "Usuário cadastrado!", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Email já cadastrado.",Toast.LENGTH_SHORT).show()
                        }
                    }




//            if (this.isEmailExistente()) {
//                Toast.makeText(applicationContext, "Usuário já cadastrado.", Toast.LENGTH_LONG).show()
//            } else {
//                val usuario = Usuario(0, "admin", email.text.toString(), null, senha.text.toString(), 0, 6199999999)
//                usuario.gravaFotoDefault(resources)
//                db?.usuarioDao()?.insertUsuario(usuario)
//                email.text.clear()
//                senha.text.clear()
//                senhaConfirmar.text.clear()
//
//                Toast.makeText(applicationContext, "Usuário cadastrado!", Toast.LENGTH_LONG).show()
//                finish()
//            }
        }
    }

    private fun isEmailExistente(): Boolean {
        var email = findViewById<EditText>(R.id.editTextEmailCadastro)

        return db?.usuarioDao()?.findByEmail(email.text.toString()) != null

    }

}
