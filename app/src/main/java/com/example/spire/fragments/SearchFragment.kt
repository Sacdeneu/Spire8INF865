package com.example.spire.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.spire.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
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
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.games_name))
        lv_listView.adapter = adapter
        lv_listView.onItemClickListener = AdapterView.OnItemClickListener { Parent, view, position, id ->
            resources.getStringArray(R.array.my_games)

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
    })

    }



}