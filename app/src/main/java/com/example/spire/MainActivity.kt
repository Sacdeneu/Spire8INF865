package com.example.spire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.spire.fragments.HomeFragment
import com.example.spire.fragments.SettingsFragment
import com.example.spire.fragments.GameSheetFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.annotation.NonNull
import com.example.spire.fragments.PopUpValidateNewOrder
import com.google.android.material.navigation.NavigationBarView




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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menulist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    fun buttonEditOrderList(menu : MenuItem){
        Toast.makeText(applicationContext,"Modification en cours", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.edit).visibility=GONE

        findViewById<View>(R.id.add).visibility=GONE
        findViewById<View>(R.id.share).visibility=GONE

        findViewById<View>(R.id.validate).visibility=VISIBLE
        findViewById<View>(R.id.cancel).visibility=VISIBLE

        findViewById<View>(R.id.move_game).visibility=VISIBLE
    }

    fun buttonEditOrderListValidate(menu : MenuItem){
        Toast.makeText(applicationContext,"Modification validé", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.validate).visibility=GONE

        findViewById<View>(R.id.cancel).visibility=GONE
        findViewById<View>(R.id.move_game).visibility=GONE

        findViewById<View>(R.id.add).visibility=VISIBLE
        findViewById<View>(R.id.share).visibility=VISIBLE
        findViewById<View>(R.id.edit).visibility=VISIBLE
    }

    fun buttonEditOrderListCancel(menu : MenuItem){
        Toast.makeText(applicationContext,"Modification annulé", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.cancel).visibility=GONE

        findViewById<View>(R.id.validate).visibility=GONE
        findViewById<View>(R.id.move_game).visibility=GONE

        findViewById<View>(R.id.add).visibility=VISIBLE
        findViewById<View>(R.id.share).visibility=VISIBLE
        findViewById<View>(R.id.edit).visibility=VISIBLE
    }

    fun buttonMoveGameList(view : View){
        Toast.makeText(applicationContext,"PopUp déplacement du jeu dans la liste", Toast.LENGTH_SHORT).show()

        val PopUpValidateNewOrder = PopUpValidateNewOrder()
        replaceFragment(PopUpValidateNewOrder)
    }


}