package com.example.spire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.*
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.annotation.NonNull
import com.google.android.material.navigation.NavigationBarView
import androidx.core.util.Pair
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.spire.databinding.ActivityMainBinding
import com.example.spire.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private var DELAY_TIME = 4000;
    private lateinit var binding:ActivityMainBinding
    val GAME_ID = "GAME_ID"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //initialise fragments
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val gameSheetFragment = GameSheetFragment()
        val SearchFragment = SearchFragment()
        val FriendsFragment = FriendsFragment()
        val UserProfilFragment = UserProfilFragment()
        val HighlightedFragment = HighlightedFragment()



        //setup toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_list)
        setSupportActionBar(toolbar)
        replaceFragment(homeFragment)

        //listener pour les boutons du menu du bas
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_best_games -> replaceFragment(HighlightedFragment)
                R.id.ic_friendlist -> replaceFragment(FriendsFragment)
                /*R.id.ic_user_profil -> replaceFragment(UserProfilFragment)*/
                R.id.ic_settings -> replaceFragment(settingsFragment)
                /*R.id.ic_game_sheet -> replaceFragment(gameSheetFragment)*/
                R.id.ic_search_game -> replaceFragment(SearchFragment)
            }
            true
        }

    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null){ //si le fragment n'est pas null on le remplace quand on change d'écran dans le menu du bas
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                commit()
            }
        }
    }

    /*override fun onSaveInstanceState(savedInstanceState: Bundle) {

    }*/

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

        //findViewById<View>(R.id.move_game).visibility=VISIBLE
    }

    fun buttonEditOrderListValidate(menu : MenuItem){
        Toast.makeText(applicationContext,"Modification validé", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.validate).visibility=GONE

        findViewById<View>(R.id.cancel).visibility=GONE
        //findViewById<View>(R.id.move_game).visibility=GONE

        findViewById<View>(R.id.add).visibility=VISIBLE
        findViewById<View>(R.id.share).visibility=VISIBLE
        findViewById<View>(R.id.edit).visibility=VISIBLE
    }

    fun buttonEditOrderListCancel(menu : MenuItem){
        Toast.makeText(applicationContext,"Modification annulé", Toast.LENGTH_SHORT).show()
        findViewById<View>(R.id.cancel).visibility=GONE

        findViewById<View>(R.id.validate).visibility=GONE
        //findViewById<View>(R.id.move_game).visibility=GONE

        findViewById<View>(R.id.add).visibility=VISIBLE
        findViewById<View>(R.id.share).visibility=VISIBLE
        findViewById<View>(R.id.edit).visibility=VISIBLE
    }

    fun buttonMoveGameList(view : View){
        Toast.makeText(applicationContext,"PopUp déplacement du jeu dans la liste", Toast.LENGTH_SHORT).show()

        val PopUpValidateNewOrder = PopUpValidateNewOrder()
        replaceFragment(PopUpValidateNewOrder)
    }
    fun adapterOnClick(game: Game) {
        val newFrag = GameSheetFragment()
        val args = Bundle()
        args.putInt(GAME_ID, game.id)
        newFrag.arguments = args
        if(newFrag != null){ //si le fragment n'est pas null on le remplace quand on change d'écran dans le menu du bas
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, newFrag)
                addToBackStack(null)
                commit()
            }
        }
    }
	
	fun buttonAddFriends(view: View){
        if(findViewById<View>(R.id.searchArea).visibility == GONE){
            findViewById<View>(R.id.searchArea).visibility = VISIBLE
        }
        else{
            findViewById<View>(R.id.searchArea).visibility = GONE
        }
    }

    fun buttonValidateSearch(view: View){
        val searchText : String = findViewById<EditText>(R.id.searchAreaField).text.toString()

        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("Username", searchText as String).get().addOnSuccessListener { query ->
                    for (queryResult in query) {
                        FirebaseFirestore.getInstance().collection("Friends").document(it1.uid)
                            .get().addOnSuccessListener { document ->
                                document.reference.update(
                                    "friends",
                                    FieldValue.arrayUnion(queryResult.get("Username"))
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Ami ajouté avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnCanceledListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Erreur : Ami(e) déjà dans la liste",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnCanceledListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Erreur : Ami(e) inexistant(e)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Erreur inconnu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun buttonFriendGameList(view: View){
        val friendId : String = findViewById<TextView>(R.id.friend_text).text.toString()

        FirebaseFirestore.getInstance().collection("GameLists").document(friendId).get()

    }
}