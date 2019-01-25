package com.example.moviemania.app.screens.movieList

import android.util.Log
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
import io.reactivex.subjects.PublishSubject

class MovieListViewModel(
    private val movieRepo: MovieRepositoryI,
    private val favRepo: FavoriteRepositoryI
) : BaseViewModel() {

    private val favoriteMoviesObservable: PublishSubject<List<Movie>> = PublishSubject.create()

    init {
        model = MovieListStateModel(
            query = listOf("hollywood", "comedy", "action", "series", "movies").random(),
            page = 1
        )
        initEvent = InitMovieListEvent
    }

    fun loadMovies() {
        loadMovies((model as MovieListStateModel).query.toString(), (model as MovieListStateModel).page)
    }

    fun paginate() {
        (model as MovieListStateModel).apply {
            if (!this.isPaginating) {
                val newPage = this.page + 1
                loadMovies(this.query!!, newPage)
                updateModel(this.copy(page = newPage, isPaginating = true))
                sendEvent(PaginatingEvent)
            }
        }
    }

    private fun loadMovies(query: String, page: Int) {
        (model as MovieListStateModel).apply {
            if (!this.isPaginating) {
                updateModel(this.copy(progress = this.progress.copy(isShown = true, text = "Loading movies...")))
            }
        }

        loadFavorites()

        favoriteMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { favoriteMovies ->
                movieRepo.loadMovies(query, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        (model as MovieListStateModel).apply {
                            it.searchItems?.let { items ->
                                val newList: ArrayList<SearchResultItem>
                                if (this.isPaginating) {
                                    newList = this.movies
                                    newList.addAll(getMergedMovies(items, favoriteMovies))
                                } else {
                                    newList = getMergedMovies(items, favoriteMovies)
                                }

                                updateModel(
                                    this.copy(
                                        movies = newList,
                                        isPaginating = false,
                                        progress = this.progress.copy(isShown = false, text = "")
                                    )
                                )
                            }
                        }
                    }.doOnError {
                        sendEvent(MovieListError(it.message.toString()))
                    }
                    .subscribe()
            }.subscribe()

    }

    private fun getMergedMovies(
        items: List<SearchResultItem>,
        favoriteMovies: List<Movie>
    ): ArrayList<SearchResultItem> {
        val list = ArrayList<SearchResultItem>()

        val favIds = favoriteMovies.map {
            it.imdbID
        }

        for (item in items) {
            if (favIds.contains(item.imdbID)) {
                list.add(item.copy(favorited = true))
            } else {
                list.add(item)
            }
        }

        return list
    }

    fun toggleMovieFavorite(item: SearchResultItem) {
        sendEvent(FavoritingMovieEvent)
        item.imdbID?.let {
            movieRepo.loadMovieDetail(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .doOnSuccess { movie ->
                    favRepo.toggleFavorite(movie)
                    sendEvent(FavoritingMovieDoneEvent)
                    loadFavorites()
                }
                .doOnError { throwable ->
                    sendEvent(FavoritingMovieError(throwable.message.toString()))
                }
                .subscribe()
        }
    }

    fun loadFavorites() {
        favRepo.loadFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { favorites ->
                favorites?.let {
                    favoriteMoviesObservable.onNext(it)
                    (model as MovieListStateModel).apply {
                        updateModel(this.copy(hasFavorites = it.isNotEmpty(), favorites = it))
                    }
                }
            }
            .subscribe()
    }

}


data class MovieListStateModel(
    val hasFavorites: Boolean = false,
    val progress: ProgressStateModel = ProgressStateModel(),

    val page: Int = 1,
    val isPaginating: Boolean = false,

    val query: String? = "",

    val favorites: List<Movie> = listOf(),
    val movies: ArrayList<SearchResultItem> = ArrayList()
) : StateModel()

sealed class MovieListEvent : ViewEvent
object InitMovieListEvent : MovieListEvent()
object FavoritingMovieEvent : MovieListEvent()
object FavoritingMovieDoneEvent : MovieListEvent()
object PaginatingEvent : MovieListEvent()
data class FavoritingMovieError(val msg: String) : MovieListEvent()
data class MovieListError(val msg: String) : MovieListEvent()