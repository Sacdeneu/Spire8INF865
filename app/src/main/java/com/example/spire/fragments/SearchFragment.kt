package com.example.spire.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.example.spire.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Retrofit
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


class SearchFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var gameAdapter: RecyclerView.Adapter<GameAdapter.GameViewHolder>? = null
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var mQueue: RequestQueue? = null
    private var mIsLoading = false
    private var mIsLastPage = false
    private var mCurrentPage = 0
    private val pageSize = 10
    private lateinit var search : String

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
        /*val gameList = Datasource(this).getGameList()
        val recyclerView: RecyclerView = recycler_Game
        recyclerView.adapter = GameAdapter(gameList)*/
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        search = binding.searchText.text.toString()

        return binding.root

    }

    private fun showAllGames(games : List<Game>){
        gameAdapter = GameAdapter(games)
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

        /*binding.recyclerGame.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0){
                    if(isLastVisable()){
                        loadMoreItems(true)
                    }
                    else{
                        loadMoreItems(false)
                    }
                }
            }
        })*/

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawg.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.fetchAllGames().enqueue(object : Callback<AllGameQuery>{
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

        api.GetPage(page).enqueue(object : Callback<AllGameQuery>{
            override fun onResponse(call: Call<AllGameQuery>, response: Response<AllGameQuery>) {
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

        api.GetPage(mCurrentPage).enqueue(object : Callback<AllGameQuery>{
            override fun onResponse(call: Call<AllGameQuery>, response: Response<AllGameQuery>) {
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
                            if(search.contains(name.toRegex())){
                                val appId = game.getInt("appid")

                                jsonParseInfoGame(appId)
                            }
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



        /*gameName.add("far Cry 6")
        gameName.add("DoodleJump")
        gameName.add("BattleField 2042")
        gameName.add("The witcher 3")
        gameName.add("Skyrim")
        gameName.add("Fifa22")
        gameName.add("Resident Evil VIllAGE")
        gameName.add("Deathloop")
        gameName.add("Halo")
        gameName.add("NBA 2K22")*/

        /*adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.games_name)
            //gameName
        )

        adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_2, mylist)
*/
        /*lv_listView.adapter = adapter
        //lv_listView2.adapter = adapter2
        lv_listView.onItemClickListener =
            AdapterView.OnItemClickListener { Parent, view, position, id ->
                Toast.makeText(
                    context,
                    Parent?.getItemAtPosition(position).toString()+" added",
                    Toast.LENGTH_SHORT
                ).show()

                mylist.add(Parent?.getItemAtPosition(position).toString())
            }



        lv_listView.emptyView = tv_emptyTextView
        search_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input

            }
        })*/





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}