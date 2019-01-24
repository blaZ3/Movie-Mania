package com.example.moviemania.dataSource.favorite

import com.example.moviemania.app.model.Movie
import io.reactivex.Single

interface FavoriteDataSourceI {

    fun checkIfFavorite(imdbOD: String): Single<Boolean>
    fun addFavorite(movie: Movie): Single<Movie>
    fun removeFavorite(movie: Movie): Single<Movie>
    fun getFavorites(): Single<List<Movie>>

}