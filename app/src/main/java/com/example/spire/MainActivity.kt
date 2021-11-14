package com.example.spire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.spire.fragments.HomeFragment
import com.example.spire.fragments.GameSheetFragment
import com.example.spire.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialise fragments
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val gameSheetFragment = GameSheetFragment()

        //setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_list)
        setSupportActionBar(toolbar)
        replaceFragment(homeFragment)

        //listener pour les boutons du menu du bas
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_game_sheet -> replaceFragment(gameSheetFragment)
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null){ //si le fragment n'est pas null on le remplace quand on change d'écran dans le menu du bas
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menulist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}