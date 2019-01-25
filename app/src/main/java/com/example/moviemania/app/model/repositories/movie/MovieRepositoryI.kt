package com.example.moviemania.app.model.repositories.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import io.reactivex.Single

interface MovieRepositoryI {
    fun loadMovies(q: String, page: Int): Single<SearchResult>
    fun loadMovieDetail(imdbID: String): Single<Movie>
}