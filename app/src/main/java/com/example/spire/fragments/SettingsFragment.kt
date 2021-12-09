package com.example.spire.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.example.spire.LoginActivity
import com.example.spire.MainActivity
import com.example.spire.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.utilities.Utilities
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SettingsFragment : PreferenceFragmentCompat() {



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
        val user = Firebase.auth.currentUser!!
        val db = FirebaseFirestore.getInstance()
        val disconnectPref = findPreference("disconnect") as Preference?
        val delete_Acount = findPreference("delete_Acount") as Preference?
        val userAcount = findPreference("id_Compte") as Preference?

        userAcount!!.setTitle(user.email)

        disconnectPref!!.setOnPreferenceClickListener {
            Firebase.auth.signOut()
            val logOutIntent = Intent(activity, LoginActivity::class.java)
            startActivity(logOutIntent)
            activity?.finish()
            true
        }

        delete_Acount!!.setOnPreferenceClickListener {
            Firebase.auth.signOut()
            db.collection("Users").document(user.uid).delete()
            db.collection("GameLists").document(user.uid).delete()
            db.collection("Friends").document(user.uid).delete()
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        /*Toast.makeText(
                        this@SettingsFragment,
                        "Compte suprimé avec succes",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    }
                }

            val logOutIntent = Intent(activity, LoginActivity::class.java)
            startActivity(logOutIntent)
            activity?.finish()
            true
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        load_settings()
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    private fun load_settings(){
        val sp = PreferenceManager.getDefaultSharedPreferences(context);

    }

}