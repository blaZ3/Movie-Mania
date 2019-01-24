package com.example.moviemania.app.model.repositories.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.dataSource.movie.MovieDataSourceI
import io.reactivex.Single

class MovieRepository(private val dataSource: MovieDataSourceI):
    MovieRepositoryI {

    override fun loadMovies(q: String, page: Int): Single<SearchResult> {
        return dataSource.getMovies(query = q, page = page)
    }

    override fun loadMovieDetail(imdbID: String): Single<Movie> {
        return dataSource.getMovie(imdbID)
    }
}