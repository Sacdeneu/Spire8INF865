package com.example.spire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AnimationStartupActivity : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var DELAY_TIME = 4000;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_animationstartup)
        firebaseAuth = FirebaseAuth.getInstance()
        //Animations
        var topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        var bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        var logo = findViewById<ImageView>(R.id.imageViewLogo)
        var background = findViewById<ImageView>(R.id.imageViewBackground);
        var textLogo = findViewById<TextView>(R.id.textView)

        logo.setAnimation(topAnim);
        background.setAnimation(bottomAnim);

        Handler().postDelayed({
            val pair1 = Pair.create<View, String>(logo, "logo_logo")
            val pair2 = Pair.create<View, String>(textLogo, "logo_text")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair2)
            //check si on est connecté, si oui, redirection sur la mainactivity, sinon, sur le login
            if(currentUser != null){
                val intentStartup = Intent(this, MainActivity::class.java);
                startActivity(intentStartup, options.toBundle());
            }
            else{
                val intentStartup = Intent(this, LoginActivity::class.java);
                startActivity(intentStartup, options.toBundle());
            }
        }, DELAY_TIME.toLong());

    }

}