package com.example.spire.fragments

data class AllGameQuery(
    val next : String,
    val previous : String,
    val results: List<Game>,
)