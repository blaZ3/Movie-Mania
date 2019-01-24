package com.example.moviemania.app.screens.movieList

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dailytools.healthbuddy.base.BaseView
import com.example.moviemania.R
import com.example.moviemania.app.base.BaseFragment
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.databinding.FragmentMovieListViewBinding
import com.example.moviemania.helpers.logger.LoggerI
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_movie_list_view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject


class MovieListFragment : BaseFragment() {

    private var listener: MovieListFragmentInteractionListener? = null
    private lateinit var dataBinding: FragmentMovieListViewBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var viewModel: MovieListViewModel
    private val logger: LoggerI by inject()
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_movie_list_view, container,
            false
        )

        progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)

        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement MovieListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun initView() {
        viewModel = get()

        recyclerMovies.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        movieListAdapter = MovieListAdapter(
            context = activity as Context, items = listOf(),
            adapterInterface = movieListAdapterInterface
        )
        recyclerMovies.adapter = movieListAdapter

        recyclerFavoriteMovies.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL,
            false
        )
        favoriteListAdapter = FavoriteListAdapter(
            context = activity as Context,
            items = listOf(),
            adapterInterface = favoriteMoviewAdapterInterface
        )
        recyclerFavoriteMovies.adapter = favoriteListAdapter

        viewModel.getViewModelObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe {
                updateView(it)
            }

        viewModel.getViewEventObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe {
                handleEvent(it)
            }
    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        logger.d("updateView", stateModel.toString())
        (stateModel as MovieListStateModel).apply {

            movieListAdapter.items = this.movies
            movieListAdapter.notifyDataSetChanged()

            favoriteListAdapter.items = this.favorites.reversed()
            favoriteListAdapter.notifyDataSetChanged()

            dataBinding.stateModel = this
        }
    }

    override fun handleEvent(event: ViewEvent) {
        logger.d("handleEvent", event.toString())
        (event as MovieListEvent).apply {
            when (this) {
                InitMovieListEvent -> {
                    viewModel.loadMovies()
                    viewModel.loadFavorites()
                }
                FavoritingMovie -> {
                    progressDialog.setTitle("Please wait")
                    progressDialog.setMessage("Adding movie to favorites")
                    progressDialog.show()
                }
                FavoritingMovieDone -> {
                    progressDialog.dismiss()
                }
                is FavoritingMovieError -> {
                    progressDialog.dismiss()
                    showToast(this.msg)
                }
                is MovieListError -> {
                    showToast(this.msg)
                }
            }
        }
    }

    private val movieListAdapterInterface = object : MovieListAdapter.MovieListAdapterInterface {
        override fun onMovieSelected(item: SearchResultItem) {
            listener?.onMovieSelected(item)
        }

        override fun onMovieFavorited(item: SearchResultItem) {
            viewModel.toggleMovieFavorite(item)
        }
    }

    private val favoriteMoviewAdapterInterface = object : FavoriteListAdapter.FavoriteListAdapterInterface {
        override fun onFavoriteMovieSelected(movie: Movie) {
            listener?.onMovieSelected(movie)
        }
    }

    interface MovieListFragmentInteractionListener {
        fun onMovieSelected(item: SearchResultItem)
        fun onMovieSelected(movie: Movie)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MovieListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
