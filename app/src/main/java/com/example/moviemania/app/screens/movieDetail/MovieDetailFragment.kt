package com.example.moviemania.app.screens.movieDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dailytools.healthbuddy.base.BaseView

import com.example.moviemania.R
import com.example.moviemania.app.base.StateModel
import com.example.moviemania.app.base.ViewEvent
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

private const val ARG_IMDBID = "ARG_IMDBID"

class MovieDetailFragment : Fragment(), BaseView {
    private var imdbId: String? = null
    private var listener: MovieDetailFragmentInteractionListener? = null


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
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
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
    }

    override fun getParentView(): BaseView? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateView(stateModel: StateModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleEvent(event: ViewEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface MovieDetailFragmentInteractionListener {
        fun onAction()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMDBID, param1)
                }
            }
    }
}
