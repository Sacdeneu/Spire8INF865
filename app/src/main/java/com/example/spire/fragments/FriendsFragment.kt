package com.example.spire.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.MainActivity
import com.example.spire.databinding.FragmentFriendsBinding
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
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var friendAdapter: RecyclerView.Adapter<FriendAdapter.FriendViewHolder>? = null
    private var _binding: FragmentFriendsBinding? = null
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
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }



    private fun showAllFriends(friends : List<Friend>){
        friendAdapter = FriendAdapter(friends) { friend -> adapterOnClickFriend(friend) }
        (friendAdapter as FriendAdapter).setList(friends)
        binding.recyclerFriends.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = friendAdapter
        }
    }

    private fun adapterOnClickFriend(friend: Friend) {
        (activity as MainActivity).adapterOnClickFriend(friend)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val friendIDList = arrayListOf<Int>() //liste des ID de jeux

        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("Friends")
                .document(it1.uid).get().addOnSuccessListener { document ->
                    //récupération data dans la friendlist
                    if(document.data != null){
                        val map: Map<String, Any?> = document.data as Map<String, Any?>
                        for ((key, value) in map) {
                            //si la clé n'est pas le UserID on ajoute l'ID du jeu dans le tableau
                            if (key != "UserID") { //on ne stocke pas le UID de l'user dans notre liste
                                if ((value.toString().toIntOrNull() != null)) {
                                    val convertedID = value as Long //long (BDD)
                                    val convertedIntID = convertedID.toInt() //int (API)
                                    friendIDList.add(convertedIntID) //on ajoute tous les ID de jeu de l'user dans friendIDList
                                }
                            }
                        }
                        val friends = friendIDList.toList() as List<Friend>
                        friendAdapter = FriendAdapter(friends) { friend -> adapterOnClickFriend(friend) }
                        //(friendAdapter as FriendAdapter).setList(friends) //ERREUR : FAIT CRASH
                        binding.recyclerFriends.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = friendAdapter
                        }
                        //transferDataToRecyclerView(friendIDList) //ASYNCHRONE -> on doit attendre que les FriendID soient prêts
                    }/*
                    else{
                        transferDataToRecyclerView(friendIDList) //ASYNCHRONE -> on doit attendre que les FriendID soient prêts
                    }*/

                }
        }
    }
/*
    @SuppressLint("CheckResult")
    private fun transferDataToRecyclerView(friendIDList: ArrayList<Int>){
        if(friendIDList.isNullOrEmpty()){ //verif en cas de bug (pas de liste associée / null passé en argument)
            binding.TextViewNoFriendAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
        }
        val friendList = arrayListOf<Friend>()
        val requests = arrayListOf<Observable<*>>()
        for(element in friendIDList) {
            val item = backendApi.GetObservableFriend(element) as Observable<*> //chaque requête de jeu à partir de son ID est stockée dans requests
            requests.add(item)
        }

        val response = it[0] // réponse cumulée de toute les requêtes

        response.forEach {friend ->
            friendList.add(friend as Friend) //chaque réponse est stockée dans la friendList
        }
        if(friendList.size > 0 ){
            binding.TextViewNoFriendAdded.visibility = View.GONE
        }
        else if(friendIDList.isNullOrEmpty() || friendList.isNullOrEmpty()){
            binding.TextViewNoFriendAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
        }
        showAllFriends(friendList) //on génère la recyclerView

    }*/


/*

    @SuppressLint("CheckResult")
    private fun transferDataToRecyclerView(friendidList: ArrayList<Int>){
        if(friendidList.isNullOrEmpty()){ //verif en cas de bug (pas de liste associée / null passé en argument)
            binding.TextViewNoFriendAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //on ajoute un adapter spécial pour prendre en compte les Observers de RxJava3
            .build()

        val backendApi = retrofit.create(ApiService::class.java)
        val friendList = arrayListOf<Friend>()
        val requests = arrayListOf<Observable<*>>()
        for(element in friendidList) {
            val item = backendApi.GetObservableFriend(element) as Observable<*> //chaque requête de jeu à partir de son ID est stockée dans requests
            requests.add(item)
        }
        Observable //on zip nos requêtes pour les executer toutes une par une de manière synchrone (car impossible de le faire de manière asynchrone dans une boucle for)
            .zip(requests) {args -> listOf(args) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({

                val response = it[0] // réponse cumulée de toute les requêtes

                response.forEach {friend ->
                    friendList.add(friend as Friend) //chaque réponse est stockée dans la friendList
                }
                if(friendList.size > 0 ){
                    binding.TextViewNoFriendAdded.visibility = View.GONE
                }
                else if(friendidList.isNullOrEmpty() || friendList.isNullOrEmpty()){
                    binding.TextViewNoFriendAdded.visibility = View.VISIBLE //message indiquant qu'il faut ajouter un jeu car la liste est vide
                }
                showAllFriends(friendList) //on génère la recyclerView
            },{
            })


    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

