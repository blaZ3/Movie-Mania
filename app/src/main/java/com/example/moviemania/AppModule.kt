package com.example.moviemania

import androidx.room.Room
import com.example.moviemania.app.db.AppDatabase
import com.example.moviemania.app.db.MovieDao
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepository
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepository
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import com.example.moviemania.app.screens.movieDetail.MovieDetailViewModel
import com.example.moviemania.app.screens.movieList.MovieListViewModel
import com.example.moviemania.dataSource.movie.DummyMovieDataSource
import com.example.moviemania.dataSource.movie.LocalMovieDataSource
import com.example.moviemania.dataSource.movie.MovieDataSourceI
import com.example.moviemania.dataSource.movie.NetworkMovieDataSource
import com.example.moviemania.helpers.logger.AppLogger
import com.example.moviemania.helpers.logger.LoggerI
import com.example.moviemania.helpers.stringFetcher.AppStringFetcher
import com.example.moviemania.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class AppModule {

    companion object {

        private val appModule = module {
            single<LoggerI> { AppLogger(BuildConfig.DEBUG) }
            single<StringFetcherI> { AppStringFetcher(androidContext()) }

            single<MovieDataSourceI>(name = "dummy") { DummyMovieDataSource() }
            single<MovieDataSourceI>(name = "network") { NetworkMovieDataSource() }
            single<MovieDataSourceI>(name = "local") { LocalMovieDataSource() }

            single<AppDatabase> {
                Room.databaseBuilder(
                    androidContext(), AppDatabase::class.java, "movies-db"
                ).build()
            }
            single { get<AppDatabase>().getMovieDao() }

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

            single<FavoriteRepositoryI> { FavoriteRepository(get()) }

        }

        private val movieListModule = module {
            viewModel { MovieListViewModel(get("dummy"), get()) }
        }

        private val movieDetailModule = module {
            viewModel { MovieDetailViewModel(get("dummy"), get()) }
        }


        val appModules = listOf(
            appModule,
            movieListModule,
            movieDetailModule
        )

    }

}