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
import com.example.spire.databinding.FragmentHighlightedBinding
import retrofit2.Call
import values.Datasource4
import retrofit2.Retrofit
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest
import com.example.spire.MainActivity
import org.json.JSONException
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory
import values.Datasource2


class HighlightedFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var gameAdapter: RecyclerView.Adapter<GameAdapter.GameViewHolder>? = null
    private var _binding: FragmentHighlightedBinding? = null
    private val binding get() = _binding!!
    private var mQueue: RequestQueue? = null
    private var mIsLoading = false
    private var mIsLastPage = false
    private var mCurrentPage = 0
    private val pageSize = 10

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialise loading state
        mIsLoading = false;
        mIsLastPage = false;
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHighlightedBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun adapterOnClick(game: Game) {
        (activity as MainActivity).adapterOnClick(game)
    }
    private fun showAllGames(games : List<Game>){
        gameAdapter = GameAdapter(games, {game -> adapterOnClick(game)})
        (gameAdapter as GameAdapter).setList(games)
        binding.recyclerGame.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = gameAdapter
        }

    }

    private fun isLastVisable(): Boolean {
        val layoutManager = binding.recyclerGame.layoutManager as LinearLayoutManager
        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems = gameAdapter!!.itemCount
        return (pos >= numItems - 1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.fetchAllGames().enqueue(object : Callback<AllGameQuery> {
            override fun onResponse(
                call: Call<AllGameQuery>,
                response: retrofit2.Response<AllGameQuery>
            ) {
                showAllGames(response.body()!!.results)

                val layoutManager = binding.recyclerGame.layoutManager as LinearLayoutManager
                scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        loadNextDataFromApi(page)
                    }
                }
                binding.recyclerGame.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener);
            }

            override fun onFailure(call: Call<AllGameQuery>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun loadNextDataFromApi(page: Int) {
// change loading state
        // change loading state
        mIsLoading = true
        Log.d("NUMBER", page.toString())
        // update recycler adapter with next page
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.GetPage(page).enqueue(object : Callback<AllGameQuery> {
            override fun onResponse(call: Call<AllGameQuery>, response: retrofit2.Response<AllGameQuery>) {
                val result = response.body()
                if(result == null)
                    return
                else
                    (gameAdapter as GameAdapter).addAll(result.results)

                mIsLoading = false
                if(result.next == null){
                    mIsLastPage = true
                }
                else{
                    mIsLastPage = false
                }
            }
            override fun onFailure(call: Call<AllGameQuery>, t: Throwable) {
            }

        })
    }

    private fun loadMoreItems(isFirstPage: Boolean) {
        // change loading state
        // change loading state
        mIsLoading = true
        mCurrentPage = mCurrentPage + 1
        Log.d("NUMBER", mCurrentPage.toString())
        // update recycler adapter with next page
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.GetPage(mCurrentPage).enqueue(object : Callback<AllGameQuery> {
            override fun onResponse(call: Call<AllGameQuery>, response: retrofit2.Response<AllGameQuery>) {
                val result = response.body()
                if(result == null)
                    return
                else if(!isFirstPage)
                    (gameAdapter as GameAdapter).addAll(result.results)

                mIsLoading = false
                if(result.next == null){
                    mIsLastPage = true
                }
                else{
                    mIsLastPage = false
                }
            }
            override fun onFailure(call: Call<AllGameQuery>, t: Throwable) {
            }

        })
    }

    private fun jsonParseListGame() {

        val url = "https://api.steampowered.com/ISteamApps/GetAppList/v2/?";

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonObject = response.getJSONObject("appList");
                    val jsonArray = jsonObject.getJSONArray("apps")
                    for (i in 0 until jsonArray.length()) {
                        val game = jsonArray.getJSONObject(i)

                        val name = game.getString("name")

                        //utilisation de l'app recup
                        /*if(search.contains(name.toRegex())){
                            val appId = game.getInt("appid")

                            jsonParseInfoGame(appId)
                        }*/
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }

        mQueue?.add(request);
    }

    private fun jsonParseInfoGame(id : Int){
        val url = "http://steamspy.com/api.php?request=appdetails&appid=$id";

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonObject = response.getJSONObject("appList");

                    val name = jsonObject.getString("name")
                    val dev = jsonObject.getString("developer")
                    val pub = jsonObject.getString("publisher")
                    val genre = jsonObject.getString("genre")
                    //val tags = jsonObject.getJSONArray("tags")


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }

        mQueue?.add(request);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


    /*private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FriendAdapter.FriendViewHolder>? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameList = Datasource4(this).getGameList()
        val recyclerView: RecyclerView = binding.recyclerGame
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GameAdapter(gameList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}*/