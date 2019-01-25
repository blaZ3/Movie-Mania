package com.example.moviemania.app.screens.movieList

import com.example.moviemania.R
import com.example.moviemania.app.base.BaseViewModel
import com.example.moviemania.app.base.ProgressStateModel
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import com.example.moviemania.helpers.stringFetcher.StringFetcherI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MovieListViewModel(
    private val movieRepo: MovieRepositoryI,
    private val favRepo: FavoriteRepositoryI,
    private val stringFetcher: StringFetcherI
) : BaseViewModel() {

    private val favoriteMoviesObservable: PublishSubject<List<Movie>> = PublishSubject.create()

    init {
        model = MovieListStateModel(
            page = 1
        )
        initEvent = InitMovieListEvent
    }

    fun getMovies() {
//        val query = listOf("hollywood", "comedy", "action", "series", "movies").random()
        val query = "hollywood"
        val page = 1
        (model as MovieListStateModel).apply {
            this.movies.clear()
            getMovies(query, page)
            updateModel(
                this.copy(
                    query = query,
                    page = page,
                    progress = this.progress.copy(isShown = true, text = stringFetcher.getString(R.string.str_loading))
                )
            )
        }
    }

    fun searchMovies(query: String) {
        (model as MovieListStateModel).apply {
            this.movies.clear()
            getMovies(query, 1)
            updateModel(this.copy(query = query, page = 1, isPaginating = false))
        }

    }

    fun paginate() {
        (model as MovieListStateModel).apply {
            if (!this.isPaginating) {
                val newPage = this.page + 1
                this.query?.let {
                    updateModel(
                        this.copy(
                            page = newPage,
                            isPaginating = true,
                            progress = this.progress.copy(isShown = false)
                        )
                    )
                    getMovies(it, newPage)
                    sendEvent(PaginatingEvent)
                }
            }
        }
    }

    private fun getMovies(query: String, page: Int) {
        loadFavorites()

        favoriteMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { favoriteMovies ->
                movieRepo.loadMovies(query, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess {
                        if (!it.response) {
                            sendEvent(MovieListNetworkError)
                        }
                        (model as MovieListStateModel).apply {
                            it.searchItems?.let { items ->
                                updateModel(
                                    this.copy(
                                        movies = getUpdatedList(this.movies, items, favoriteMovies),
                                        isPaginating = false,
                                        progress = this.progress.copy(isShown = false, text = "")
                                    )
                                )
                            }
                        }
                    }.doOnError {
                        sendEvent(MovieListError(it.message.toString()))
                    }.doOnError { it.printStackTrace() }
                    .subscribe()
            }.doOnError { it.printStackTrace() }
            .subscribe()

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
            }.doOnError { it.printStackTrace() }
            .subscribe()
    }

    private fun getUpdatedList(
        oldItems: ArrayList<SearchResultItem>,
        newItems: List<SearchResultItem>,
        favoriteMovies: List<Movie>
    ): ArrayList<SearchResultItem> {

        val updateList = oldItems
        for (item in getMergedMovies(newItems, favoriteMovies)) {
            var updated: Boolean = false
            for (i in 0 until updateList.size) {
                if (updateList[i].imdbID == item.imdbID) {
                    updateList[i] = item
                    updated = true
                    break
                }
            }
            if (!updated) {
                updateList.add(item)
            }
        }

        return updateList
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
object MovieListNetworkError : MovieListEvent()
data class FavoritingMovieError(val msg: String) : MovieListEvent()
data class MovieListError(val msg: String) : MovieListEvent()