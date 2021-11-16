package com.example.spire.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.example.spire.R
import com.google.firebase.database.core.utilities.Utilities

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)

        val notificationPreference: CheckBoxPreference? = findPreference("DarkMode")
        notificationPreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                // Manually save to Prefs
                val sh = requireActivity().applicationContext.getSharedPreferences("DarkMode", Context.MODE_PRIVATE).edit();
                if(newValue == true) {
                    sh.putBoolean("is",true);
                    sh.commit();
                    val ft = requireFragmentManager().beginTransaction()
                    ft.detach(this).attach(this).commit()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else {
                    sh.putBoolean("is",false);
                    sh.commit();
                    val ft = requireFragmentManager().beginTransaction()
                    ft.detach(this).attach(this).commit()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                // Reflect the newValue to Preference?
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