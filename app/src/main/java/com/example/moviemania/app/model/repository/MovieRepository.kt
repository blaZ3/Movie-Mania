package com.example.moviemania.app.model.repository

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.dataSource.MovieDataSourceI
import io.reactivex.Single

class MovieRepository(private val dataSource: MovieDataSourceI,
                      private val localDataSource: MovieDataSourceI):
    MovieRepositoryI {


    override fun loadMovies(q: String): Single<SearchResult> {
        return dataSource.getMovies()
    }

    override fun loadMovies(q: String, page: Int): Single<SearchResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMovieDetail(imdbID: String): Single<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}