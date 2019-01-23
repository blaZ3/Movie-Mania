package com.example.moviemania.dataSource

import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.helpers.TestHelper
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class DummyMovieDataSource: MovieDataSourceI {


    override fun getMovies(): Single<SearchResult> {
        return Single.just(TestHelper.getDummySearchResult())
            .delay(1500, TimeUnit.MILLISECONDS)
    }
}