package com.example.moviemania

import com.example.moviemania.app.model.repository.MovieRepository
import com.example.moviemania.app.model.repository.MovieRepositoryI
import com.example.moviemania.app.screens.movieList.MovieListViewModel
import com.example.moviemania.dataSource.DummyMovieDataSource
import com.example.moviemania.dataSource.LocalMovieDataSource
import com.example.moviemania.dataSource.MovieDataSourceI
import com.example.moviemania.dataSource.NetworkMovieDataSource
import com.example.moviemania.helpers.stringFetcher.AppStringFetcher
import com.example.moviemania.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class AppModule {

    companion object {

        private val appModule = module {
            single<StringFetcherI> { AppStringFetcher(androidContext()) }

            single<MovieDataSourceI>(name = "dummy") { DummyMovieDataSource() }
            single<MovieDataSourceI>(name = "network") { NetworkMovieDataSource() }
            single<MovieDataSourceI>(name = "local") { LocalMovieDataSource() }

            single<MovieRepositoryI>(name = "dummy") {
                MovieRepository(
                    dataSource = get("dummy"), localDataSource = get("local")
                )
            }
            single<MovieRepositoryI>(name = "network") {
                MovieRepository(
                    dataSource = get("network"), localDataSource = get("local")
                )
            }

        }

        private val movieListModule = module {
            viewModel { MovieListViewModel(get("dummy")) }
        }

        val appModules = listOf(
            appModule,
            movieListModule
        )

    }

}