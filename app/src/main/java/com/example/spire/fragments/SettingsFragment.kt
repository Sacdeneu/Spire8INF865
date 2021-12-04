package com.example.spire.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.example.spire.LoginActivity
import com.example.spire.MainActivity
import com.example.spire.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.utilities.Utilities
import com.google.firebase.ktx.Firebase

class SettingsFragment : PreferenceFragmentCompat() {



    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
        val disconnectPref = findPreference("disconnect") as Preference?
        disconnectPref!!.setOnPreferenceClickListener {
            Firebase.auth.signOut()
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