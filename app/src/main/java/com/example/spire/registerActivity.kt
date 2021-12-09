package com.example.spire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.spire.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.HashMap







class registerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnRegister.setOnClickListener{
            when {
                TextUtils.isEmpty(binding.mail.text.toString().trim{it <= ' ' }) -> {
                    Toast.makeText(
                        this@registerActivity,
                        "please enter mail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password.text.toString().trim{it <= ' ' }) -> {
                    Toast.makeText(
                        this@registerActivity,
                        "please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    val mail: String= binding.mail.text.toString().trim(){it<= ' ' }
                    val password: String= binding.password.text.toString().trim(){it<= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{task ->
                                if(task.isSuccessful) {
                                    //val firebaseUser: FirebaseUser = task.result!!.user!!

                                    /*Toast.makeText(
                                        this@registerActivity,
                                        "Succesfull register",
                                        Toast.LENGTH_SHORT
                                    ).show()*/

                                    val user = hashMapOf(
                                        "imageURL" to "http://image.jpg",
                                        "Username" to binding.mail.text.toString()
                                    )
                                    FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                    FirebaseFirestore.getInstance().collection("Users").document(it1.uid)
                                        .set(user)
                                        .addOnSuccessListener { documentReference ->
                                            Toast.makeText(
                                                this@registerActivity,
                                                "Sucessfully in database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this@registerActivity,
                                                "Error Database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    val gamelist = hashMapOf(
                                        "UserID" to (FirebaseAuth.getInstance().currentUser?.uid)
                                    )
                                    FirebaseFirestore.getInstance().collection("GameLists").add(gamelist)

                                    val friends = hashMapOf(
                                        "Friend0" to ""
                                    )
                                    FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                        FirebaseFirestore.getInstance().collection("Friends")
                                            .document(it1.uid).collection("FriendLists").add(friends)
                                    }
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this@registerActivity,
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