package com.example.moviemania

import com.example.moviemania.app.model.repositories.favorite.FavoriteRepositoryI
import com.example.moviemania.app.model.repositories.movie.MovieRepository
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import com.example.moviemania.app.screens.movieList.MovieListViewModel
import com.example.moviemania.dataSource.movie.DummyMovieDataSource
import com.example.moviemania.helpers.TestHelper
import com.example.moviemania.helpers.TestHelper.Companion.TEST_PAGE
import com.example.moviemania.helpers.TestHelper.Companion.TEST_QUERY
import com.example.moviemania.helpers.TestHelper.Companion.TEST_STRING
import com.example.moviemania.helpers.stringFetcher.StringFetcherI
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class MovieListViewModelTests {

    private lateinit var viewModel: MovieListViewModel

    private val movieRepo: MovieRepositoryI = mock()
    private val favRepo: FavoriteRepositoryI = mock()
    private val stringFetcher: StringFetcherI = mock()


    @Test
    fun `when viewModel getMovies no errors happen`() {
        whenever(favRepo.loadFavorites()).doReturn(Single.just(listOf()))
        whenever(stringFetcher.getString(R.string.str_loading)).doReturn(TEST_STRING)
        viewModel = MovieListViewModel(
            movieRepo = movieRepo,
            favRepo = favRepo,
            stringFetcher = stringFetcher
        )
        val testObserver = viewModel.getViewModelObservable().test()

        viewModel.getMovies()

        testObserver.assertNoErrors()
    }

    @Test
    fun `when viewModel getMovies modelObservable emits once`() {
        whenever(favRepo.loadFavorites()).doReturn(Single.just(listOf()))
        whenever(movieRepo.loadMovies(TEST_QUERY, TEST_PAGE)).doReturn(Single.just(TestHelper.getDummySearchResult()))

        whenever(stringFetcher.getString(R.string.str_loading)).doReturn(TEST_STRING)
        viewModel = MovieListViewModel(
            movieRepo = movieRepo,
            favRepo = favRepo,
            stringFetcher = stringFetcher
        )
        val testObserver = viewModel.getViewModelObservable().test()

        viewModel.getMovies()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
    }


    @Test
    fun `when viewModel getMovies with dummy JSON values modelObservable should emit once`() {
        whenever(favRepo.loadFavorites()).doReturn(Single.just(listOf()))

        val movieRepo = MovieRepository(
            dataSource = DummyMovieDataSource(),
            localFavoriteDataSource = mock()
        )

        whenever(stringFetcher.getString(R.string.str_loading)).doReturn(TestHelper.TEST_STRING)
        viewModel = MovieListViewModel(
            movieRepo = movieRepo,
            favRepo = favRepo,
            stringFetcher = stringFetcher
        )

        val testObserver = viewModel.getViewModelObservable().test()

        testObserver
            .assertNoErrors()
            .assertValueCount(1)
    }

}