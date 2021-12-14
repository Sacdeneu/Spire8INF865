package com.example.spire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.spire.databinding.ActivityMainBinding
import com.example.spire.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private var DELAY_TIME = 4000;
    private lateinit var binding:ActivityMainBinding
    val GAME_ID = "GAME_ID"
    val USER_ID = "USER_ID"
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
    fun adapterOnClickFriend(friendID: String) {
        val newFrag = UserProfilFragment()
        val args = Bundle()
        args.putString(USER_ID, friendID)
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
        val friendName : String = findViewById<TextView>(R.id.friend_text).text.toString()
        var friendId : String = ""

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("Username",friendName as String)
            .get().addOnSuccessListener { query ->
                for (queryResult in query) {
                    friendId = queryResult.id
                    adapterOnClickFriend(friendId)
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    "ERROR",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    fun buttonDeleteFriend(view: View){
        val friendName : String = findViewById<TextView>(R.id.user_name).text.toString()

        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("Username", friendName as String).get().addOnSuccessListener { query ->
                    for (queryResult in query) {
                        FirebaseFirestore.getInstance().collection("Friends").document(it1.uid)
                            .get().addOnSuccessListener { document ->
                                document.reference.update(
                                    "friends",
                                    FieldValue.arrayRemove(queryResult.get("Username"))
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Ami supprimé avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnCanceledListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Erreur",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnCanceledListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Erreur",
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


}