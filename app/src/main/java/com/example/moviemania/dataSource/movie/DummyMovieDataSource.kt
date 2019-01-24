package com.example.moviemania.dataSource.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.helpers.TestHelper
import com.example.moviemania.helpers.TestHelper.Companion.DUMMY_DELAY
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class DummyMovieDataSource: MovieDataSourceI {



    override fun getMovies(): Single<SearchResult> {
        return Single.just(TestHelper.getDummySearchResult())
            .delay(DUMMY_DELAY, TimeUnit.MILLISECONDS)
    }

    override fun getMovie(imdbId: String): Single<Movie> {
        return Single.just(TestHelper.getDummyMovieDetail())
            .delay(DUMMY_DELAY, TimeUnit.MILLISECONDS)
    }

}