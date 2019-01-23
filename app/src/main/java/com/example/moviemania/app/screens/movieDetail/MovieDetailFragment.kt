package com.example.moviemania.app.screens.movieDetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.moviemania.R

private const val ARG_IMDBID = "ARG_IMDBID"

class MovieDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imdbId: String? = null
    private var listener: MovieDetailFragmentInteractionListener? = null

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
