package com.example.moviemania.app.screens.movieList

import com.example.moviemania.app.base.BaseViewModel
import com.example.moviemania.app.base.ProgressStateModel
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieListViewModel(private val movieRepo: MovieRepositoryI,
                         private val favRepo: FavoriteRepositoryI): BaseViewModel(){

    private val query = "the big bang"

    init {
        model = MovieListStateModel()
        initEvent = InitMovieListEvent
    }

    fun loadMovies() {
        (model as MovieListStateModel).apply {
            updateModel(this.copy(progress = this.progress.copy(isShown = true, text = "Loading movies...")))
        }


        movieRepo.loadMovies(query)
            .doOnSuccess {
                (model as MovieListStateModel).apply {
                    it.searchItems?.let { items ->
                        updateModel(this.copy(movies = items,
                            progress = this.progress.copy(isShown = false, text = "")))
                    }
                }
            }.doOnError {
                sendEvent(MovieListError(it.message.toString()))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

    fun toggleMovieFavorite(item: SearchResultItem) {
        sendEvent(FavoritingMovie)
        item.imdbID?.let {
            movieRepo.loadMovieDetail(it)
                .doOnSuccess { movie ->
                    favRepo.toggleFavorite(movie)
                    sendEvent(FavoritingMovieDone)
                    loadFavorites()
                }
                .doOnError { throwable ->
                    sendEvent(FavoritingMovieError(throwable.message.toString()))
                }.subscribe()
        }
    }

    fun loadFavorites(){
        favRepo.loadFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { favorites ->
                favorites?.let {
                    (model as MovieListStateModel).apply {
                        updateModel(this.copy(hasFavorites = it.isNotEmpty(), favorites = it))
                    }

                }
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
object FavoritingMovie: MovieListEvent()
object FavoritingMovieDone: MovieListEvent()
data class FavoritingMovieError(val msg: String): MovieListEvent()
data class MovieListError(val msg: String): MovieListEvent()