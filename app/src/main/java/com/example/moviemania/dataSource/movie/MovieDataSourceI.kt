package com.example.moviemania.dataSource.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import io.reactivex.Single

interface MovieDataSourceI {

    fun getMovies(query: String, page: Int): Single<SearchResult>
    fun getMovie(imdbId: String): Single<Movie>

}