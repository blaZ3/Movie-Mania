package com.example.moviemania.app.screens.movieList

import com.example.moviemania.app.base.BaseViewModel
import com.example.moviemania.app.base.ProgressStateModel
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.model.repository.MovieRepositoryI

class MovieListViewModel(private val movieRepo: MovieRepositoryI): BaseViewModel(){

    private val query = "the big bang"

    init {
        model = MovieListStateModel()
        initEvent = InitMovieListEvent
    }

    fun loadMovies() {
        movieRepo.loadMovies(query)
            .doOnSuccess {
                (model as MovieListStateModel).apply {
                    it.searchItems?.let { items ->
                        updateModel(this.copy(movies = items))

                    }
                }
            }.doOnError {
                sendEvent(MovieListError(it.message.toString()))
            }.subscribe()
    }

}


data class MovieListStateModel(
    val hasFavorites: Boolean = false,
    val progress: ProgressStateModel = ProgressStateModel(),
    val isPaginating: Boolean = false,

    val favorites: List<Movie> = listOf(),
    val movies: List<SearchResultItem> = listOf()
): StateModel()

sealed class MovieListEvent: ViewEvent
object InitMovieListEvent: MovieListEvent()
class MovieListError(msg: String): MovieListEvent()