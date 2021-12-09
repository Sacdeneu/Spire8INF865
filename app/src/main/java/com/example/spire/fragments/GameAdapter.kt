package com.example.spire.fragments

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.R
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.coroutineContext

class GameAdapter(private val gameList: List<Game>, private val onClick: (Game) -> Unit) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var gamelist = arrayListOf<Game>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<Game>) { //set la liste interne à l'adapter
        gamelist = list as ArrayList<Game>
        notifyDataSetChanged()
    }


    fun addAll(newList: List<Game>) { //ajoute tous les éléments d'une liste à la liste interne de l'adapter
        val lastIndex: Int = gameList.size - 1
        gamelist.addAll(newList)
        notifyItemRangeInserted(lastIndex, newList.size)
    }
    class GameViewHolder(itemView: View, val onClick: (Game) -> Unit) : RecyclerView.ViewHolder(itemView){
        private val gameTextView: TextView = itemView.findViewById(R.id.game_text)
        private val buttonView: Button = itemView.findViewById(R.id.game_sheet_button)
        private val gameImage: ImageView = itemView.findViewById(R.id.game_image)
        private val publisherTextView: TextView = itemView.findViewById(R.id.game_publisher)

        private var currentGame: Game? = null

        init {
            itemView.setOnClickListener {
                currentGame?.let {
                    onClick(it)
                }
            }
        }
        //binding, modifie le nom du jeu pour la valeur dans l'API et ajoute un listener on click sur le bouton voir le jeu
        fun bind(game: Game){
            gameTextView.text = game.name
            Picasso.get().load(game.background_image).into(gameImage)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rawg.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //on récupère grace à l'ID passé en argument les détails du jeu et on les affiche
            val api = retrofit.create(ApiService::class.java)
            api.GetGame(game.id).enqueue(object : Callback<Game> {
                override fun onResponse(
                    call: Call<Game>,
                    response: retrofit2.Response<Game>
                ) {
                    Log.d("response", response.body().toString())
                    if(response.body()!!.publishers[0] != null)
                        publisherTextView.text = response.body()!!.publishers[0]!!.name
                }

                override fun onFailure(call: Call<Game>, t: Throwable) {
                }

            })
            buttonView.setOnClickListener {
                onClick(game)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card, parent, false)
        return GameViewHolder(view, onClick)
    }
    override fun getItemCount(): Int {
        return gameList.size
    }
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(gameList[position])
    }
}
