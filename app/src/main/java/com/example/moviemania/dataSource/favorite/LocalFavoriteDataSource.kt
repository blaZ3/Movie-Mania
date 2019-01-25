package com.example.moviemania.dataSource.favorite

import com.example.moviemania.app.db.MovieDao
import com.example.moviemania.app.model.INVALID_ID
import com.example.moviemania.app.model.Movie
import io.reactivex.Single

class LocalFavoriteDataSource(private val movieDao: MovieDao) : FavoriteDataSourceI {

    override fun checkIfFavorite(imdbID: String): Single<Boolean> {
        return Single.create { emitter ->
            val favorites = movieDao.getById(imdbID)
            if (favorites.isNotEmpty()) {
                emitter.onSuccess(true)
            } else {
                emitter.onSuccess(false)
            }
        }
    }

    override fun toggleFavorite(movie: Movie): Single<Movie> {
        return Single.create { emitter ->
            val favorites = movie.imdbID?.let { movieDao.getById(it) }
            favorites?.let {
                if (it.isNotEmpty()) {
                    movie.id = it[0].id
                    movieDao.delete(movie)
                } else {
                    movieDao.insertAll(movie)
                }
            }
            emitter.onSuccess(movie)
        }
    }

    override fun getFavorites(): Single<List<Movie>> {
        return Single.create { emitter ->
            emitter.onSuccess(movieDao.getAll())
        }
    }

    override fun getFromFavorites(imdbID: String): Single<Movie> {
        return Single.create { emitter ->
            val favorites = movieDao.getById(imdbID)
            if (favorites.isNotEmpty()){
                emitter.onSuccess(favorites[0])
            }else{
                emitter.onSuccess(Movie(id = INVALID_ID))
            }
        }
    }
}