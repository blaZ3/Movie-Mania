package com.example.moviemania

import com.example.moviemania.app.model.SearchResult
import com.example.moviemania.app.model.repositories.movie.MovieRepository
import com.example.moviemania.app.model.repositories.movie.MovieRepositoryI
import com.example.moviemania.dataSource.favorite.FavoriteDataSourceI
import com.example.moviemania.dataSource.movie.DummyMovieDataSource
import com.example.moviemania.dataSource.movie.MovieDataSourceI
import com.example.moviemania.dataSource.movie.NetworkMovieDataSource
import com.example.moviemania.helpers.TestHelper
import com.example.moviemania.helpers.networkHelper.NetworkHelperI
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepositoryTests {

    private lateinit var networkDataSource: MovieDataSourceI
    private lateinit var localFavoriteDataSource: FavoriteDataSourceI
    private lateinit var movieRepo: MovieRepositoryI
    private lateinit var networkHelper: NetworkHelperI

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        localFavoriteDataSource = mock()
        networkHelper = mock()

        movieRepo = MovieRepository(
            dataSource = DummyMovieDataSource(),
            localFavoriteDataSource = localFavoriteDataSource
        )

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }


    @Test
    fun `when movieRepo loadMovies called with dummyDataSource it should not fail and emit once`() {
        val testObserver = movieRepo.loadMovies(q = TestHelper.TEST_QUERY, page = TestHelper.TEST_PAGE).test()
        Thread.sleep(TestHelper.DUMMY_DELAY + 100)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }


    @Test
    fun `when loadMovies response should be false when not connected to network`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestHelper.dummySearcResultJson)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        whenever(networkHelper.isConncected()).doReturn(false)

        networkDataSource = NetworkMovieDataSource(
            retrofit, apiKey = TestHelper.API_KEY, networkHelper = networkHelper
        )

        movieRepo = MovieRepository(dataSource = networkDataSource, localFavoriteDataSource = localFavoriteDataSource)
        val testObserver = movieRepo.loadMovies(TestHelper.TEST_QUERY, TestHelper.TEST_PAGE).test()
        Thread.sleep(100)
        testObserver
            .assertValueCount(1)
            .assertNoErrors()
            .assertResult(SearchResult(response = false))
    }

    @Test
    fun `when movieRepo loadMovies called with 200 OK response and network connected`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestHelper.dummySearcResultJson)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        whenever(networkHelper.isConncected()).doReturn(true)

        networkDataSource = NetworkMovieDataSource(
            retrofit, apiKey = TestHelper.API_KEY, networkHelper = networkHelper
        )

        movieRepo = MovieRepository(dataSource = networkDataSource, localFavoriteDataSource = localFavoriteDataSource)
        val testObserver = movieRepo.loadMovies(TestHelper.TEST_QUERY, TestHelper.TEST_PAGE).test()
        Thread.sleep(100)
        testObserver
            .assertValueCount(1)
            .assertNoErrors()
            .assertResult(TestHelper.getDummySearchResult())
    }

    @Test
    fun `when movieRepo loadMovies called with 500  response and network connected`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(TestHelper.dummySearcResultJson)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        whenever(networkHelper.isConncected()).doReturn(false)

        networkDataSource = NetworkMovieDataSource(
            retrofit, apiKey = TestHelper.API_KEY, networkHelper = networkHelper
        )

        movieRepo = MovieRepository(dataSource = networkDataSource, localFavoriteDataSource = localFavoriteDataSource)
        val testObserver = movieRepo.loadMovies(TestHelper.TEST_QUERY, TestHelper.TEST_PAGE).test()
        Thread.sleep(100)
        testObserver
            .assertValueCount(1)
            .assertNoErrors()
            .assertResult(SearchResult(response = false))
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

}