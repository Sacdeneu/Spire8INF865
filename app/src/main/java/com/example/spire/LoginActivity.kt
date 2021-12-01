package com.example.spire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login)


        val btn_create_account = findViewById(R.id.createAccountButton) as Button
        btn_create_account.setOnClickListener {
            val intentCreateAccount = Intent(this, RegisterActivity::class.java);
            startActivity(intentCreateAccount);
        }
    }
}