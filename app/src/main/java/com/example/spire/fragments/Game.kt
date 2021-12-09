package com.example.spire.fragments

import java.util.*

data class Game(
    val id : Int, //id du jeu
    val name : String, //nom du jeu
    val release : Date, // date de sortie
    val released : Date, // date de sortie (jeu détaillé)
    val background_image : String, // image de fond
    val description_raw : String, // description texte
    val publishers : List<Publisher>, // liste d'éditeurs
    val developers : List<Developer> // liste de dévelopeurs
    )