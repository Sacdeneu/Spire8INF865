package com.example.spire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.Toast
import com.example.spire.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View


class LoginActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

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

        val btnLoginAccount = findViewById<Button>(R.id.loginButton)
        btnLoginAccount.setOnClickListener(this::login)
        val btnCreateAccount = findViewById<Button>(R.id.createAccountButton)
        btnCreateAccount.setOnClickListener {
            val intentCreateAccount = Intent(this, registerActivity::class.java);
            startActivity(intentCreateAccount);
        }


    }

}