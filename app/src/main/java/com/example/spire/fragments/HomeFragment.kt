package com.example.spire.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.MainActivity
import com.example.spire.R
import com.example.spire.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var homeGameAdapter: RecyclerView.Adapter<GameAdapter.GameViewHolder>? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



    private fun showAllGames(games : List<Game>){
        homeGameAdapter = GameAdapter(games, true) { game -> adapterOnClick(game) }
        (homeGameAdapter as GameAdapter).setList(games)
        binding.recyclerHomeGame.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeGameAdapter
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
        if(gameidList.isNullOrEmpty()){ //verif en cas de bug (pas de liste associée / null passé en argument)
            binding.TextViewNoGameAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
        }
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
                if(gameList.size > 0 ){
                    binding.TextViewNoGameAdded.visibility = View.GONE
                }
                else if(gameidList.isNullOrEmpty() || gameList.isNullOrEmpty()){
                    binding.TextViewNoGameAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
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

