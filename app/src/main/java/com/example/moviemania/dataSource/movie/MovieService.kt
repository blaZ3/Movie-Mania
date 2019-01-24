package com.example.moviemania.dataSource.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/")
    fun search(@Query("apikey") apiKey: String,
               @Query("s")q: String,
               @Query("page")page: String = "1"): Call<SearchResult>

    @GET("/")
    fun movieDetail(@Query("apikey")apiKey: String,
                    @Query("i")imdbID: String): Call<Movie>

}