package com.example.moviemania.app.model.repositories.favorite

import com.example.moviemania.app.db.MovieDao
import com.example.moviemania.app.model.Movie
import io.reactivex.Single

class FavoriteRepository(private val movieDao: MovieDao): FavoriteRepositoryI {

    override fun isFavorite(imdbId: String): Single<Boolean> {
        return Single.create { emitter ->
            val favorites = movieDao.getById(imdbId)
            if (favorites.isNotEmpty()){
                emitter.onSuccess(true)
            }else{
                emitter.onSuccess(false)
            }
        }
    }

    override fun setAsFavorite(movie: Movie) {
        movieDao.insertAll(movie)
    }

    override fun loadFavorites(): Single<List<Movie>> {
        return Single.create { emitter ->
            emitter.onSuccess(movieDao.getAll())
        }
    }

}