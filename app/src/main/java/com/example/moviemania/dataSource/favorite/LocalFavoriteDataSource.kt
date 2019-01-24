package com.example.moviemania.dataSource.favorite

import com.example.moviemania.app.model.Movie
import io.reactivex.Single

class LocalFavoriteDataSource: FavoriteDataSourceI {

    override fun checkIfFavorite(imdbOD: String): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFavorite(movie: Movie): Single<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFavorite(movie: Movie): Single<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFavorites(): Single<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}