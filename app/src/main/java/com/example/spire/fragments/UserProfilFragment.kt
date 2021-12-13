package com.example.spire.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.spire.R
import com.example.spire.databinding.FragmentUserProfilBinding
import retrofit2.Call
import values.Datasource5
import retrofit2.Retrofit
import com.android.volley.RequestQueue;
import com.example.spire.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.*


class UserProfilFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FriendAdapter.FriendViewHolder>? = null
    private var gameAdapter: RecyclerView.Adapter<GameAdapter.GameViewHolder>? = null
    private var _binding: FragmentUserProfilBinding? = null
    private val binding get() = _binding!!
    public val mylist = arrayListOf<String>()
    private var mQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQueue = Volley.newRequestQueue(this.context);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfilBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameList = Datasource5(this).getGameList()
        val recyclerView: RecyclerView = binding.recyclerGame
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GameAdapter(gameList)
        }
    }*/

    private fun showAllGames(games : List<Game>){
        gameAdapter = GameAdapter(games) { game -> adapterOnClick(game) }
        (gameAdapter as GameAdapter).setList(games)
        binding.recyclerGame.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = gameAdapter
        }
    }

    private fun adapterOnClick(game: Game) {
        (activity as MainActivity).adapterOnClick(game)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameIDList = arrayListOf<Int>() //liste des ID de jeux

        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("GameLists")
                .document(it1.uid).get().addOnSuccessListener { document ->
                    //récupération data dans la gamelist
                    if(document.data != null){
                        val map: Map<String, Any?> = document.data as Map<String, Any?>
                        for ((key, value) in map) {
                            //si la clé n'est pas le UserID on ajoute l'ID du jeu dans le tableau
                            if (key != "UserID") { //on ne stocke pas le UID de l'user dans notre liste
                                if ((value.toString().toIntOrNull() != null)) {
                                    val convertedID = value as Long //long (BDD)
                                    val convertedIntID = convertedID.toInt() //int (API)
                                    gameIDList.add(convertedIntID) //on ajoute tous les ID de jeu de l'user dans gameIDList
                                }
                            }
                        }
                        transferDataToRecyclerView(gameIDList) //ASYNCHRONE -> on doit attendre que les GameID soient prêts
                    }
                    else{
                        transferDataToRecyclerView(gameIDList) //ASYNCHRONE -> on doit attendre que les GameID soient prêts
                    }

                }
        }
    }



    @SuppressLint("CheckResult")
    private fun transferDataToRecyclerView(gameidList: ArrayList<Int>){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //on ajoute un adapter spécial pour prendre en compte les Observers de RxJava3
            .build()

        val backendApi = retrofit.create(ApiService::class.java)
        val gameList = arrayListOf<Game>()
        val requests = arrayListOf<Observable<*>>()
        for(element in gameidList) {
            val item = backendApi.GetObservableGame(element) as Observable<*> //chaque requête de jeu à partir de son ID est stockée dans requests
            requests.add(item)
        }
        Observable //on zip nos requêtes pour les executer toutes une par une de manière synchrone (car impossible de le faire de manière asynchrone dans une boucle for)
            .zip(requests) {args -> listOf(args) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({

                val response = it[0] // réponse cumulée de toute les requêtes

                response.forEach {game ->
                    gameList.add(game as Game) //chaque réponse est stockée dans la gameList
                }
                showAllGames(gameList) //on génère la recyclerView
            },{
            })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}