package com.example.spire.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spire.R
//import kotlinx.android.synthetic.main.fragment_home.home
import android.widget.ImageButton
import android.widget.RatingBar
import com.example.spire.databinding.ActivityMainBinding
import com.example.spire.databinding.FragmentGameSheetBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameSheetFragment : Fragment(), RatingBar.OnRatingBarChangeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var game_score_personal: Double = 0.0
    private val score_list: MutableList<Double> = mutableListOf(2.0, 3.0, 4.0, 5.0)
    private val nb_notes = score_list.lastIndex

    private var _binding: FragmentGameSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameSheetBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_game_sheet, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameAdvancement.text = "Terminé"
        binding.gameAdvancement.setTextColor(Color.parseColor("#0aad3f"))
        binding.gameScoreUser.text = "4,0 \n /5"
        binding.gameRatingBar.onRatingBarChangeListener = this    //Ecoute les changements de note
        averageScore(score_list)
    }


    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {  //Lorsque la note du jeu change :
        game_score_personal = p1.toDouble()
        //game_score_personal_text.text = game_score_personal.toString()
        if( score_list.lastIndex == nb_notes ) {
            score_list.add(game_score_personal)
        } else {
            score_list[score_list.lastIndex] = game_score_personal
        }
        averageScore(score_list)
    }

    fun averageScore(score_list: MutableList<Double>) { //Calcule et affiche le score moyen du jeu
        var average_score = 0.0
        var total_score = 0.0
        for (i in 0..(score_list.size-1)) {
            println(score_list[i])
            total_score += score_list[i]
        }
        average_score = ((((total_score / score_list.size)* 100).toInt()).toDouble() / 100) //Calcul le score moyen
        binding.gameScorePublic.text = "$average_score \n /5"   //Affiche le score de la moyenne de tous les utilisateurs
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}