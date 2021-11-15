package com.example.spire.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spire.R
import kotlinx.android.synthetic.main.fragment_home.*
//import kotlinx.android.synthetic.main.fragment_home.home
import kotlinx.android.synthetic.main.fragment_game_sheet.*
import android.widget.ImageButton
import android.widget.RatingBar
import kotlinx.android.synthetic.main.fragment_game_sheet.game_advancement as game_advancement1

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
    private val score_list: MutableList<Double> = mutableListOf(1.0, 2.0, 3.0)

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_sheet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game_advancement.text = "Terminé"
        game_advancement.setTextColor(Color.parseColor("#0aad3f"))
        game_score_user.text = "4,0 \n /5"
        game_rating_bar.onRatingBarChangeListener = this    //Ecoute les changements de note
    }


    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {  //Lorsque la note du jeu change :
        game_score_personal = p1.toDouble()
        //game_score_personal_text.text = game_score_personal.toString()
        score_list.add(game_score_personal)
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
        game_score_public.text = "$average_score \n    /5"   //Affiche le score de la moyenne de tous les utilisateurs
    }
}