package com.example.moviemania.app.model.repositories.favorite

import com.example.moviemania.app.model.Movie
import io.reactivex.Single

interface FavoriteRepositoryI {

    fun isFavorite(imdbId: String): Single<Boolean>
    fun setAsFavorite(movie: Movie)
    fun loadFavorites(): Single<List<Movie>>

}