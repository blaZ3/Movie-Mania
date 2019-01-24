package com.example.moviemania.app.model.repositories.favorite

import com.example.moviemania.app.model.Movie
import com.example.moviemania.dataSource.favorite.FavoriteDataSourceI
import io.reactivex.Single

class FavoriteRepository(private val dataSource: FavoriteDataSourceI): FavoriteRepositoryI {

    override fun isFavorite(imdbId: String): Single<Boolean> {
        return dataSource.checkIfFavorite(imdbId)
    }

    override fun toggleFavorite(movie: Movie) {
        dataSource.toggleFavorite(movie).subscribe()
    }

    override fun loadFavorites(): Single<List<Movie>> {
        return dataSource.getFavorites()
    }

}