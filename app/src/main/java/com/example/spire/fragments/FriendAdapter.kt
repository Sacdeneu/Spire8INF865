package com.example.spire.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.spire.R
import androidx.fragment.app.Fragment
import com.example.spire.MainActivity
import com.example.spire.registerActivity

class FriendAdapter(val friendList: Array<*>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val friendTextView: TextView = itemView.findViewById(R.id.friend_text)

        fun bind(word: Any?){
            friendTextView.text = word.toString()
            /*Toast.makeText(
                itemView.context, "test2", Toast.LENGTH_SHORT
            ).show()*/
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        /*Toast.makeText(
            view.context, "test3", Toast.LENGTH_SHORT
        ).show()*/
        return FriendViewHolder(view)
    }
    override fun getItemCount(): Int {
        return friendList.size
    }
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}