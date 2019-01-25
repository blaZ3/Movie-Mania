package com.example.moviemania.app.screens.movieList

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dailytools.healthbuddy.base.BaseView
import com.example.moviemania.R
import com.example.moviemania.app.base.BaseFragment
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.example.moviemania.app.screens.movieList.adapter.*
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

        movieListAdapter = MovieListAdapter(
            context = activity as Context, items = listOf(),
            adapterInterface = movieListAdapterInterface
        )

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerMovies.layoutManager = layoutManager
        recyclerMovies.adapter = movieListAdapter
        recyclerMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                logger.d("onScrolled", "last visible item position ${layoutManager.findLastVisibleItemPositions(null)[0]}" )
                logger.d("onScrolled", "item count ${layoutManager.itemCount}" )

                if(layoutManager.findLastVisibleItemPositions(null)[0] >= layoutManager.itemCount-2){
                    viewModel.paginate()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

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

            val updatedItems = ArrayList<MovieListDataItem>()
            if (this.hasFavorites) {
                updatedItems.add(
                    MovieListDataItem(
                        TYPE_FAVORITES,
                        item = this.favorites.reversed()
                    )
                )
            }
            updatedItems.addAll(this.movies.map {
                MovieListDataItem(
                    TYPE_MOVIE,
                    item = it
                )
            })

            val diffResult = DiffUtil.calculateDiff(MovieListDiffCallback(movieListAdapter.items,
                updatedItems))
            diffResult.dispatchUpdatesTo(movieListAdapter)

            movieListAdapter.items = updatedItems

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
                FavoritingMovieEvent -> {
                    progressDialog.setTitle("Please wait")
                    progressDialog.setMessage("Adding movie to favorites")
                    progressDialog.show()
                }
                FavoritingMovieDoneEvent -> {
                    progressDialog.dismiss()
                }
                PaginatingEvent -> {
                    showToast("Loading more, please wait...")
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

    fun search(query: String){
        viewModel.loadMovies(query)
    }

    private val movieListAdapterInterface = object : MovieListAdapter.MovieListAdapterInterface {
        override fun onMovieSelectedAtPosition(position: Int) {
            listener?.onMovieSelected(movieListAdapter.items[position].item as SearchResultItem)
        }

        override fun onMovieFavoritedAtPosition(position: Int) {
            viewModel.toggleMovieFavorite(movieListAdapter.items[position].item as SearchResultItem)
        }

        override fun onMovieSelected(item: SearchResultItem) {
            listener?.onMovieSelected(item)
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
