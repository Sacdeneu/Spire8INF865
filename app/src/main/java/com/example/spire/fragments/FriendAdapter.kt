package com.example.spire.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
//import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.coroutineContext

class FriendAdapter(private val friendList: List<Friend>, private val onClick: (Friend) -> Unit) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private var friendlist = arrayListOf<Friend>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<Friend>) { //set la liste interne à l'adapter
        friendlist = list as ArrayList<Friend>
        notifyDataSetChanged()
    }


    fun addAll(newList: List<Friend>) { //ajoute tous les éléments d'une liste à la liste interne de l'adapter
        val lastIndex: Int = friendList.size - 1
        friendlist.addAll(newList)
        notifyItemRangeInserted(lastIndex, newList.size)
    }
    class FriendViewHolder(itemView: View, val onClick: (Friend) -> Unit) : RecyclerView.ViewHolder(itemView){
        private val friendTextView: TextView = itemView.findViewById(R.id.friend_text)
        //private val buttonView: LinearLayout = itemView.findViewById(R.id.friend)
        /*private val buttonViewAddFriend: Button = itemView.findViewById(R.id.addFriendButton)*/
        private val friendImage: ImageView = itemView.findViewById(R.id.friend_image)
        /*private val publisherTextView: TextView = itemView.findViewById(R.id.friend_score)*/

        private var currentFriend: Friend? = null

        init {
            itemView.setOnClickListener {
                currentFriend?.let {
                    onClick(it)
                }
            }
        }
        //binding, modifie le nom du jeu pour la valeur dans l'API et ajoute un listener on click sur le bouton voir le jeu
        fun bind(friend: Friend){
            friendTextView.text = friend.name
            /*Picasso.get().load(friend.background_image).into(friendImage)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rawg.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            /*//on récupère grace à l'ID passé en argument les détails du jeu et on les affiche
            val api = retrofit.create(ApiService::class.java)
            api.GetFriend(friend.id).enqueue(object : Callback<Friend> {
                override fun onResponse(
                    call: Call<Friend>,
                    response: retrofit2.Response<Friend>
                ) {
                    Log.d("response", response.body().toString())
                    if(response.body()!!.publishers[0] != null)
                        publisherTextView.text = response.body()!!.publishers[0]!!.name
                    buttonViewAddFriend.setOnClickListener {
                        FirebaseAuth.getInstance().currentUser?.let { it1 ->
                            FirebaseFirestore.getInstance().collection("FriendLists").document(it1.uid)
                                .update("${response.body()!!.name} ID", friend.id)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(ContentValues.TAG, "added")
                                }
                        }

                    }
                }

                override fun onFailure(call: Call<Friend>, t: Throwable) {
                }

            })*/
            buttonView.setOnClickListener {
                onClick(friend)
            }*/

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        return FriendViewHolder(view, onClick)
    }
    override fun getItemCount(): Int {
        return friendList.size
    }
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}
