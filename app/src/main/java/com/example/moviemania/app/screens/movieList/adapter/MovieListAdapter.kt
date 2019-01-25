package com.example.moviemania.app.screens.movieList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviemania.R
import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResultItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_favorites.view.*
import kotlinx.android.synthetic.main.layout_item_movie.view.*


class MovieListAdapter(
    var items: List<MovieListDataItem>,
    private val context: Context,
    adapterInterface: MovieListAdapterInterface
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    init {
        movieListAdapterInterface = adapterInterface
    }

    abstract class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class MovieFavoritesViewHolder(private val view: View, private val context: Context) : MovieListViewHolder(view) {
        fun onBind(item: MovieListDataItem) {
            view.recyclerFavoriteMovies.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false)
            val favoriteListAdapter = FavoriteListAdapter(
                context = context,
                items = item.item as List<Movie>,
                adapterInterface = favoriteMovieAdapterInterface!!
            )

            view.recyclerFavoriteMovies.adapter = favoriteListAdapter
        }
    }

    class MovieItemViewHolder(private val view: View) : MovieListViewHolder(view) {

        fun onBind(item: SearchResultItem) {
            view.txtMovieCardName.text = item.title ?: ""
            view.txtMovieCardYear.text = item.year ?: ""
            if (!item.poster.isNullOrBlank() && !item.poster.isNullOrEmpty()) {
                Picasso.get().load(item.poster).into(view.imgMovieCardPoster)
            }

            view.imgMovieCardDoFavoriteDone.visibility = View.GONE
            view.imgMovieCardDoFavorite.visibility = View.GONE
            if (item.favorited) {
                view.imgMovieCardDoFavoriteDone.visibility = View.VISIBLE
            } else {
                view.imgMovieCardDoFavorite.visibility = View.VISIBLE
            }

            view.imgMovieCardPoster.setOnClickListener {
                movieListAdapterInterface?.onMovieSelectedAtPosition(adapterPosition)
            }
            view.txtMovieCardName.setOnClickListener {
                movieListAdapterInterface?.onMovieSelectedAtPosition(adapterPosition)
            }

            view.imgMovieCardDoFavorite.setOnClickListener {
                movieListAdapterInterface?.onMovieFavoritedAtPosition(adapterPosition)
            }
            view.imgMovieCardDoFavoriteDone.setOnClickListener {
                movieListAdapterInterface?.onMovieFavoritedAtPosition(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        if (viewType == TYPE_FAVORITES) {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_item_favorites, parent,
                false)

            return MovieFavoritesViewHolder(
                view,
                context
            )
        }

        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_movie, parent, false)
        return MovieItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        if (holder is MovieItemViewHolder) {
            holder.onBind(item = items[position].item as SearchResultItem)
        } else if (holder is MovieFavoritesViewHolder) {
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                holder.itemView.layoutParams
            )
            layoutParams.isFullSpan = true
            holder.itemView.layoutParams = layoutParams

            holder.onBind(item = items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }


    override fun getItemCount(): Int {
        return items.size
    }


    companion object {
        private var movieListAdapterInterface: MovieListAdapterInterface? = null
        private var favoriteMovieAdapterInterface = object : FavoriteListAdapter.FavoriteListAdapterInterface {
            override fun onFavoriteMovieSelected(movie: Movie) {
                movieListAdapterInterface?.onMovieSelected(
                    SearchResultItem(
                        title = movie.title,
                        imdbID = movie.imdbID
                    )
                )
            }
        }
    }

    interface MovieListAdapterInterface {
        fun onMovieSelectedAtPosition(position: Int)
        fun onMovieSelected(item: SearchResultItem)

//        fun onMovieFavorited(item: SearchResultItem)
        fun onMovieFavoritedAtPosition(position: Int)
    }

}

data class MovieListDataItem(
    val type: Int,
    val item: Any
)

const val TYPE_FAVORITES = 1
const val TYPE_MOVIE = 2