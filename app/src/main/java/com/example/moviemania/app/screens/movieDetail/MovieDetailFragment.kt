package com.example.moviemania.app.screens.movieDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dailytools.healthbuddy.base.BaseView
import com.example.moviemania.R
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import com.example.moviemania.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import org.koin.android.ext.android.get

private const val ARG_IMDBID = "ARG_IMDBID"

class MovieDetailFragment : Fragment(), BaseView {

    private var imdbId: String? = null
    private var listener: MovieDetailFragmentInteractionListener? = null

    private lateinit var dataBinding: FragmentMovieDetailBinding

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imdbId = it.getString(ARG_IMDBID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container,
            false)
        return dataBinding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieDetailFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun initView() {
        viewModel = get()

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


        imgMovieDetailFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
        imgMovieDetailFavoriteDone.setOnClickListener {
            viewModel.toggleFavorite()
        }

    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        (stateModel as MovieDetailStateModel).apply {
            if (!this.movie?.poster.isNullOrEmpty() && !this.movie?.poster.isNullOrBlank()){
                Picasso.get().load(this.movie?.poster).into(imgMovieDetailPoster)
            }
            dataBinding.stateModel = this
        }
    }

    override fun handleEvent(event: ViewEvent) {
        (event as MovieDetailViewEvent).apply {
            when (this) {
                InitMovieDetailEvent -> {
                    imdbId?.let { viewModel.getMovie(it) }
                }
            }
        }
    }

    interface MovieDetailFragmentInteractionListener {
        fun onAction()
    }

    companion object {
        @JvmStatic
        fun newInstance(imdbId: String) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMDBID, imdbId)
                }
            }
    }
}
