package com.example.moviemania.dataSource

import com.example.moviemania.app.model.SearchResult
import io.reactivex.Single

interface MovieDataSourceI {

    fun getMovies(): Single<SearchResult>

}