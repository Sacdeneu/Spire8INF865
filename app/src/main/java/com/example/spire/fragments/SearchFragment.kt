package com.example.spire.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import com.example.spire.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_game_sheet.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private lateinit var adapter:ArrayAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.game_array))
        lv_listView.adapter = adapter
        /*lv_ListView.onItemClickListener =*/
        lv_listView.emptyView = tv_emptyTextView
        /*search_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

            override fun afterTextChanged(editable: Editable) {
            //after the change calling the method and passing the search input
            filter(editable.toString())
        }
    })*/

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_search,menu)

        val Search =menu?.findItem(R.id.nav_search)
        val SearchView = Search?.actionView as SearchView
        SearchView.queryHint = "que recherchez vous ?"

        SearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

    }
}