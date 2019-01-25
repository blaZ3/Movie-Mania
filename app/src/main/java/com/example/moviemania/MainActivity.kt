package com.example.moviemania

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.screens.movieDetail.MovieDetailFragment
import com.example.moviemania.app.screens.movieList.MovieListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    MovieListFragment.MovieListFragmentInteractionListener, MovieDetailFragment.MovieDetailFragmentInteractionListener {

    private lateinit var movieListFragment: MovieListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

        movieListFragment = MovieListFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, movieListFragment)
            .commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            if (Intent.ACTION_SEARCH == it.action) {
                val query = it.getStringExtra(SearchManager.QUERY)
                movieListFragment.search(query)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }


    override fun onMovieSelected(item: SearchResultItem) {
        item.imdbID?.let { navigateToMovieDetail(it) }
    }

    override fun onMovieSelected(movie: Movie) {
        movie.imdbID?.let { navigateToMovieDetail(it) }
    }

    override fun onAction() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun navigateToMovieDetail(imdgId: String) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("HOME")
            .replace(R.id.mainContainer, MovieDetailFragment.newInstance(imdgId))
            .commit()
    }


    private fun initToolbar() {
        setSupportActionBar(toolbarMain)
        supportActionBar?.title = ""
        toolbarMain.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
        toolbarMain.setNavigationOnClickListener { onBackPressed() }
    }
}
