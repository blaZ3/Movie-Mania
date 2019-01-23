package com.example.moviemania.app.screens.movieDetail

import com.example.moviemania.app.base.BaseViewModel
import com.example.moviemania.app.base.ProgressStateModel
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.repository.MovieRepositoryI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel(private val movieRepo: MovieRepositoryI): BaseViewModel() {

    init {
        model = MovieDetailStateModel()
        initEvent = InitMovieDetailEvent
    }


    fun getMovie(id: String){
        (model as MovieDetailStateModel).apply {


            updateModel(this.copy(progress = this.progress.copy(isShown = true, text = "Loading movie details...")))

            movieRepo.loadMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {

                    it?.let { movie ->
                        updateModel(this.copy(movie = movie, progress = this.progress.copy(isShown = false, text = "")))
                    }
                }
                .doOnError {

                }.subscribe()
        }
    }

}

data class MovieDetailStateModel(
    val progress: ProgressStateModel = ProgressStateModel(),

    val movie: Movie? = null
): StateModel()


sealed class MovieDetailViewEvent: ViewEvent
object InitMovieDetailEvent: MovieDetailViewEvent()