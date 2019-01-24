package com.example.moviemania

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.screens.movieDetail.MovieDetailFragment
import com.example.moviemania.app.screens.movieList.MovieListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    MovieListFragment.MovieListFragmentInteractionListener, MovieDetailFragment.MovieDetailFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, MovieListFragment.newInstance())
            .commit()
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
