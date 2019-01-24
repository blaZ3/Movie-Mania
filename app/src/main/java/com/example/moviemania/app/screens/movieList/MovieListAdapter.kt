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
    var items: List<SearchResultItem>,
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
            if (!item.poster.isNullOrBlank() && !item.poster.isNullOrEmpty()) {
                Picasso.get().load(item.poster).into(view.imgMovieCardPoster)
            }

            view.imgMovieCardPoster.tag = item
            view.txtMovieCardName.tag = item
            view.imgMovieCardDoFavorite.tag = item
            view.imgMovieCardDoFavoriteDone.tag = item

            view.imgMovieCardDoFavoriteDone.visibility = View.GONE
            view.imgMovieCardDoFavorite.visibility = View.GONE
            if (item.favorited){
                view.imgMovieCardDoFavoriteDone.visibility = View.VISIBLE
            } else{
                view.imgMovieCardDoFavorite.visibility = View.VISIBLE
            }

            view.imgMovieCardPoster.setOnClickListener {
                movieListAdapterInterface?.onMovieSelected(it.tag as SearchResultItem)
            }
            view.txtMovieCardName.setOnClickListener {
                movieListAdapterInterface?.onMovieSelected(it.tag as SearchResultItem)
            }

            view.imgMovieCardDoFavorite.setOnClickListener {
                movieListAdapterInterface?.onMovieFavorited(it.tag as SearchResultItem)
            }
            view.imgMovieCardDoFavoriteDone.setOnClickListener {
                movieListAdapterInterface?.onMovieFavorited(it.tag as SearchResultItem)
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
        fun onMovieFavorited(item: SearchResultItem)
    }

}