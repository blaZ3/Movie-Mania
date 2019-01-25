package com.example.moviemania.dataSource.favorite

import com.example.moviemania.app.model.Movie
import io.reactivex.Single

interface FavoriteDataSourceI {

    fun checkIfFavorite(imdbID: String): Single<Boolean>
    fun toggleFavorite(movie: Movie): Single<Movie>
    fun getFavorites(): Single<List<Movie>>
    fun getFromFavorites(imdbID: String): Single<Movie>

}