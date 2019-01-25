package com.example.moviemania.app.screens.movieDetail

import com.example.moviemania.app.base.BaseViewModel
import com.example.moviemania.app.base.ProgressStateModel
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel(private val movieRepo: MovieRepositoryI,
                           private val favRepo: FavoriteRepositoryI): BaseViewModel() {

    init {
        model = MovieDetailStateModel()
        initEvent = InitMovieDetailEvent
    }


    fun getMovie(id: String){
        (model as MovieDetailStateModel).apply {
            updateModel(this.copy(progress = this.progress.copy(isShown = true, text = "Loading movie details...")))
        }

        movieRepo.loadMovieDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it?.let { movie ->
                    (model as MovieDetailStateModel).apply {
                        updateModel(this.copy(movie = movie, progress = this.progress.copy(isShown = false, text = "")))
                    }
                }
            }
            .doOnError {

            }.subscribe()


       setFavoriteOrNot(id)

    }

    fun toggleFavorite() {
        (model as MovieDetailStateModel).apply{
            Observable.create<Any> { _ ->
                favRepo.toggleFavorite(this.movie!!)
                this.movie.imdbID?.let { it -> setFavoriteOrNot(it) }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    private fun setFavoriteOrNot(id: String) {
        favRepo.isFavorite(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                (model as MovieDetailStateModel).apply {
                    updateModel(this.copy(isFavorite = it))
                }
            }.subscribe()
    }

}

data class MovieDetailStateModel(
    val progress: ProgressStateModel = ProgressStateModel(),

    val isFavorite: Boolean? = false,

    val movie: Movie? = null
): StateModel()


sealed class MovieDetailViewEvent: ViewEvent
object InitMovieDetailEvent: MovieDetailViewEvent()