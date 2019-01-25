package com.example.moviemania.app.model.repositories.movie

import com.example.moviemania.app.model.INVALID_ID
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.dataSource.favorite.FavoriteDataSourceI
import com.example.moviemania.dataSource.movie.MovieDataSourceI
import io.reactivex.Single

class MovieRepository(
    private val dataSource: MovieDataSourceI,
    private val localFavoriteDataSource: FavoriteDataSourceI
) :MovieRepositoryI {

    override fun loadMovies(q: String, page: Int): Single<SearchResult> {
        return dataSource.getMovies(query = q, page = page)
    }

    override fun loadMovieDetail(imdbID: String): Single<Movie> {

        return Single.create { emitter ->
            localFavoriteDataSource.getFromFavorites(imdbID)
                .doOnSuccess {
                    if (it.id != INVALID_ID){
                        emitter.onSuccess(it.copy(response = true))
                    }else{
                        dataSource.getMovie(imdbID)
                            .doOnSuccess { movie ->
                                emitter.onSuccess(movie)
                            }.subscribe()
                    }
                }.doOnError {
                    it.printStackTrace()
                }
                .subscribe()
        }

//        return Single.concat(
//            dataSource.getMovie(imdbID),
//            localFavoriteDataSource.getFromFavorites(imdbID)
//        ).filter { it.id != INVALID_ID }
//            .first(Movie())
    }
}