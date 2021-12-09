package com.example.spire.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.R

class GameAdapter(private val gameList: List<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var gamelist = arrayListOf<Game>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<Game>) {
        gamelist = list as ArrayList<Game>
        notifyDataSetChanged()
    }

    fun addAll(newList: List<Game>) {
        val lastIndex: Int = gameList.size - 1
        gamelist.addAll(newList)
        notifyItemRangeInserted(lastIndex, newList.size)
    }
    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val gameTextView: TextView = itemView.findViewById(R.id.game_text)
        fun bind(word: Game){
            gameTextView.text = word.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card, parent, false)
        return GameViewHolder(view)
    }
    override fun getItemCount(): Int {
        return gameList.size
    }
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(gameList[position])
    }
}
