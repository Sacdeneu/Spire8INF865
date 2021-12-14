package com.example.spire.fragments

import io.reactivex.rxjava3.core.Observable
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

    @GET("/api/games/{id}?key=b35f1a8dfd0f48e2a6379d1f942637b8")
    fun GetObservableGame(@Path("id") id: Int?): Observable<Game>


    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&page=1&page_size=10")
    fun fetchObservableAllGames(): Observable<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&page=1&page_size=10")
    fun GetObservableSearchGames(@Query("search") searchQuery : String): Observable<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&page_size=10")
    fun SearchGames(@Query("search") searchQuery : String): Call<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&dates=2021-11-15,2022-01-15&ordering=-added&page=1&page_size=5")
    fun fetchCloseToReleaseGames(): Call<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&dates=2021-11-15,2022-01-15&ordering=-added&page_size=5")
    fun GetPageCloseToRelease(@Query("page") nbPage: Int): Call<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&ordering=-metacritic&page=1&page_size=5")
    fun fetchBestGames(): Call<AllGameQuery>

    @GET("/api/games?key=b35f1a8dfd0f48e2a6379d1f942637b8&ordering=-metacritic&page_size=5")
    fun GetPageBest(@Query("page") nbPage: Int): Call<AllGameQuery>

}