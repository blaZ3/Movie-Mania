package com.example.moviemania.dataSource.movie

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.helpers.networkHelper.NetworkHelperI
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NetworkMovieDataSource(
    retrofit: Retrofit, private val apiKey: String,
    private val networkHelper: NetworkHelperI
) : MovieDataSourceI {

    private var service: MovieService = retrofit.create(MovieService::class.java)

    override fun getMovies(query: String, page: Int): Single<SearchResult> {
        return Single.create { emitter ->
            if (networkHelper.isConncected()) {
                service.search(apiKey, q = query, page = page.toString()).enqueue(object : Callback<SearchResult> {
                    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                        response.body()?.let {
                            if (response.isSuccessful && it.response!!) {
                                emitter.onSuccess(it)
                            } else {
                                emitter.onSuccess(SearchResult(response = false))
                            }
                        }
                    }
                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        emitter.onError(t)
                    }
                })
            } else {
                emitter.onSuccess(SearchResult(response = false))
            }

        }

    }

    override fun getMovie(imdbId: String): Single<Movie> {
        return Single.create { emitter ->
            if (networkHelper.isConncected()) {
                service.movieDetail(apiKey, imdbID = imdbId).enqueue(object : Callback<Movie> {
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        response.body()?.let {
                            if (response.isSuccessful && it.response) {
                                emitter.onSuccess(it)
                            } else {
                                emitter.onSuccess(Movie(response = false))
                            }
                        }
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        emitter.onError(t)
                    }
                })
            } else {
                emitter.onSuccess(Movie(response = false))
            }
        }
    }
}