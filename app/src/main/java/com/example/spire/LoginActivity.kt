package com.example.spire

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spire.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class LoginActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private fun passwordForgot(view: android.view.View){

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.layout_alertbox_passwordforgot, null)

            val editText  = dialogLayout.findViewById<EditText>(R.id.mdpOublieEditText)
            builder.setTitle("Mot de passe oublié ?")
            builder.setMessage("Veuillez rentrer votre adresse e-mail pour obtenir un message de récupération")
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(editText.text.toString().trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "E-mail de récupération envoyé", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            builder.setNegativeButton("Annuler") { dialog, which -> dialog.dismiss() }
            builder.show()
        }


    }


    private fun login(view: android.view.View){
        val email= binding.editTextEmailAddress.text.toString()
        val password= binding.editTextPassword.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth= FirebaseAuth.getInstance()

        val btnLoginAccount = binding.loginButton
        btnLoginAccount.setOnClickListener(this::login)
        val btnPassWordForgot = binding.passwordForgotButton
        btnPassWordForgot.setOnClickListener(this::passwordForgot)
        val btnCreateAccount = findViewById<Button>(R.id.createAccountButton)
        btnCreateAccount.setOnClickListener {
            val intentCreateAccount = Intent(this, registerActivity::class.java);
            startActivity(intentCreateAccount);
        }


    }

}