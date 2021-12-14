package com.example.spire.fragments

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.spire.R
import com.example.spire.databinding.FragmentFriendsBinding
import retrofit2.Call
//import values.Datasource3
import retrofit2.Retrofit
import com.android.volley.RequestQueue;
import com.example.spire.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class FriendsFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FriendAdapter.FriendViewHolder>? = null
    private var _binding: FragmentFriendsBinding? = null
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
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val friendList = Datasource3(this).getFriendList()

        /*val doc = FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("Friends")
                .document(it1.uid)
        }
        if (doc != null) {
            val friends = doc.get().result?.get("friends")
            val friendList = (friends as ArrayList<String>).toArray() as Array<String>
            val recyclerView: RecyclerView = binding.recyclerFriends
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = FriendAdapter(friendList)
            }
        }*/
        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            FirebaseFirestore.getInstance().collection("Friends")
                .document(it1.uid).get().addOnSuccessListener {
                    if(it.get("friends") is ArrayList<*>){
                        val friendList = (it.get("friends") as ArrayList<*>).toArray() as Array<*>
                        val recyclerView: RecyclerView = binding.recyclerFriends
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = FriendAdapter(friendList)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        activity,
                        "ERROR",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        /*Toast.makeText(
            activity, "test", Toast.LENGTH_SHORT
        ).show()*/
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

