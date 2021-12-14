package com.example.spire.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GameAdapter(private val gameList: List<Game>, private val isHome : Boolean, private val onClick: (Game) -> Unit) :
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
        private val gameScoreTextView: TextView = itemView.findViewById(R.id.game_score)
        private val buttonView: GridLayout = itemView.findViewById(R.id.Game)
        private val buttonViewAddGame: Button = itemView.findViewById(R.id.addGameButton)
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
            gameScoreTextView.text = (game.metacritic.toFloat() /20).toString() + " / 5"

            Picasso.get().load(game.background_image).into(gameImage)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rawg.io")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

            //on récupère grace à l'ID passé en argument les détails du jeu et on les affiche
            val api = retrofit.create(ApiService::class.java)

            val gameList = arrayListOf<Game>()
            val requests = arrayListOf<Observable<*>>()
            val item = api.GetObservableGame(game.id) as Observable<*> //chaque requête de jeu à partir de son ID est stockée dans requests
            requests.add(item)
            Observable //on zip nos requêtes pour les executer toutes une par une de manière synchrone (car impossible de le faire de manière asynchrone dans une boucle for)
                .zip(requests) {args -> listOf(args) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({

                    val response = it[0] // réponse cumulée de toute les requêtes

                    response.forEach { gameID ->
                        gameList.add(gameID as Game) //chaque réponse est stockée dans la gameList
                    }


                    if (gameList[0] != null) {
                        publisherTextView.text = gameList[0].publishers[0].name
                    }


                    buttonViewAddGame.setOnClickListener {
                        FirebaseAuth.getInstance().currentUser?.let { it1 ->
                            FirebaseFirestore.getInstance().collection("GameLists")
                                .document(it1.uid)
                                .update("${gameList[0].name} ID", game.id)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(ContentValues.TAG, "added")
                                    Toast.makeText(itemView.context, "Jeu ajouté à votre liste", Toast.LENGTH_SHORT).show()
                                }
                        }

                    }
                    buttonView.setOnClickListener {
                        onClick(game)
                    }
                },{
            })
    }}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card, parent, false)

        if(isHome){
            view.findViewById<View>(R.id.addGameButton).visibility = View.GONE
            view.findViewById<View>(R.id.deleteGameButton).visibility = View.VISIBLE
        }
        else{
            view.findViewById<View>(R.id.addGameButton).visibility = View.VISIBLE
            view.findViewById<View>(R.id.deleteGameButton).visibility = View.GONE
        }

        return GameViewHolder(view, onClick)
    }
    override fun getItemCount(): Int {
        return gameList.size
    }
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(gameList[position])
    }
}
