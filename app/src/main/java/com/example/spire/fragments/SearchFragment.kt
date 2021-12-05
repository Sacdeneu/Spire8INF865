package com.example.spire.fragments

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.spire.PlaceHolderApi1
import com.example.spire.R
import com.example.spire.databinding.FragmentSearchBinding
import org.json.JSONArray
import retrofit2.Call
import values.Datasource
import retrofit2.Retrofit
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.spire.MainActivity

import org.json.JSONException;
import org.json.JSONObject;






class SearchFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<GameAdapter.GameViewHolder>? = null
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    //private lateinit var adapter: ArrayAdapter<*>
   // private lateinit var adapter2: ArrayAdapter<*>
    public val mylist = arrayListOf<String>()
    val gameName = arrayListOf<String>()
    private var mQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mQueue = Volley.newRequestQueue(this.context);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*val gameList = Datasource(this).getGameList()
        val recyclerView: RecyclerView = recycler_Game
        recyclerView.adapter = GameAdapter(gameList)*/

        _binding = FragmentSearchBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameList = Datasource(this).getGameList()
        val recyclerView: RecyclerView = binding.recyclerGame
        //recyclerView.adapter = GameAdapter(gameList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GameAdapter(gameList)
        }

        fun jsonParseListGame() {

            val url = "https://api.steampowered.com/ISteamApps/GetAppList/v2/?";

            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val jsonObject = response.getJSONObject("appList");
                        val jsonArray = jsonObject.getJSONArray("apps")
                        for (i in 0 until jsonArray.length()) {
                            val game = jsonArray.getJSONObject(i)
                            val appId = game.getString("appid")
                            val name = game.getInt("name")

                            //utilisation de l'app recup
                        }
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


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}