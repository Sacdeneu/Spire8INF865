package com.example.spire.fragments

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&page=1&page_size=10")
    fun fetchAllGames(): Call<AllGameQuery>

    @GET("{URLPage}")
    fun URLPage(@Path("URLPage") urlPage: String): Call<AllGameQuery>


    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&page_size=10")
    fun GetPage(@Query("page") nbPage: Int): Call<AllGameQuery>

    @GET("/api/games/{id}?key=b35f1a8dfd0f48e2a6379d1f942637b8")
    fun GetGame(@Path("id") id: Int?):Call<Game>

}