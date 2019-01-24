package com.example.moviemania

import androidx.room.Room
import com.example.moviemania.app.db.AppDatabase
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepository
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepository
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import com.example.moviemania.app.screens.movieDetail.MovieDetailViewModel
import com.example.moviemania.app.screens.movieList.MovieListViewModel
import com.example.moviemania.dataSource.movie.DummyMovieDataSource
import com.example.moviemania.dataSource.movie.MovieDataSourceI
import com.example.moviemania.dataSource.movie.NetworkMovieDataSource
import com.example.moviemania.helpers.logger.AppLogger
import com.example.moviemania.helpers.logger.LoggerI
import com.example.moviemania.helpers.stringFetcher.AppStringFetcher
import com.example.moviemania.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule {

    companion object {

        private val appModule = module {
            single<LoggerI> { AppLogger(BuildConfig.DEBUG) }
            single<StringFetcherI> { AppStringFetcher(androidContext()) }

            single<Retrofit> {
                Retrofit.Builder()
                    .baseUrl(BuildConfig.ROOT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            single<MovieDataSourceI>(name = "dummy") { DummyMovieDataSource() }
            single<MovieDataSourceI>(name = "network") {
                NetworkMovieDataSource(
                    retrofit = get(), apiKey = BuildConfig.API_KEY
                )
            }

            single<AppDatabase> {
                Room.databaseBuilder(
                    androidContext(), AppDatabase::class.java, "movies-db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            single { get<AppDatabase>().getMovieDao() }

            single<MovieRepositoryI>{ (name: String) ->
                MovieRepository(dataSource = get(name))
            }

            single<FavoriteRepositoryI> { FavoriteRepository(get()) }

        }

        private val movieListModule = module {
            viewModel { MovieListViewModel(get{ parametersOf("network")}, get()) }
        }

        private val movieDetailModule = module {
            viewModel { MovieDetailViewModel(get{ parametersOf("network") }, get()) }
        }


        val appModules = listOf(
            appModule,
            movieListModule,
            movieDetailModule
        )

    }

}