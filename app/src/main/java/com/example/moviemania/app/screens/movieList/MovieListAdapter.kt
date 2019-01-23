package com.example.moviemania.app.screens.movieList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemania.R
import com.example.moviemania.app.model.SearchResultItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_movie.view.*

class MovieListAdapter(
    private val items: List<SearchResultItem>,
    private val context: Context,
    adapterInterface: MovieListAdapterInterface
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    init {
        movieListAdapterInterface = adapterInterface
    }

    class MovieListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun onBind(item: SearchResultItem) {
            view.txtMovieCardName.text = item.title ?: ""
            view.txtMovieCardYear.text = item.year ?: ""
            if (item.poster.isNullOrBlank() && item.poster.isNullOrEmpty()) {
                Picasso.get().load(item.poster).into(view.imgMovieCardPoster)
            }

            view.tag = item
            view.setOnClickListener {
                movieListAdapterInterface?.onMovieSelected(it.tag as SearchResultItem)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_movie, parent, false)
        return MovieListViewHolder(view)
    }


    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.onBind(item = items[position])
    }


    override fun getItemCount(): Int {
        return items.size
    }


    companion object {
        private var movieListAdapterInterface: MovieListAdapterInterface? = null
    }

    interface MovieListAdapterInterface {
        fun onMovieSelected(item: SearchResultItem)
    }

}