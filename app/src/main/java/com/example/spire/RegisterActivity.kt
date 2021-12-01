package com.example.spire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

private var mAuth: FirebaseAuth? = null
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener{
            when {
                TextUtils.isEmpty(mail.toString().trim{it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "please enter mail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(password.toString().trim{it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    val mail: String= mail.toString().trim(){it<= ' ' }
                    val password: String= password.toString().trim(){it<= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{task ->
                                if(task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Succesfull register",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT).show()
                                }

                            }
                        )
                }
            }
        }

    }


}