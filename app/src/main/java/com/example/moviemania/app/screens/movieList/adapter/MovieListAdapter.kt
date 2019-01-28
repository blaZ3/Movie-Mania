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
import java.lang.ref.WeakReference


class MovieListAdapter(
    var items: List<MovieListDataItem>,
    context: Context,
    private val movieListAdapterInterface: MovieListAdapterInterface
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    private var weakContext: WeakReference<Context> = WeakReference(context)

    abstract class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class MovieFavoritesViewHolder(
        private val view: View,
        private val adapterInterface: FavoriteListAdapter.FavoriteListAdapterInterface
    ) : MovieListViewHolder(view) {
        fun onBind(item: MovieListDataItem) {
            view.recyclerFavoriteMovies.layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.HORIZONTAL,
                false
            )
            val favoriteListAdapter = FavoriteListAdapter(
                context = view.context,
                items = item.item as List<Movie>,
                adapterInterface = adapterInterface
            )

            view.recyclerFavoriteMovies.adapter = favoriteListAdapter
        }
    }

    class MovieItemViewHolder(
        private val view: View,
        private val movieListAdapterInterface: MovieListAdapterInterface
    ) : MovieListViewHolder(view) {

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
                movieListAdapterInterface.onMovieSelectedAtPosition(adapterPosition)
            }
            view.txtMovieCardName.setOnClickListener {
                movieListAdapterInterface.onMovieSelectedAtPosition(adapterPosition)
            }

            view.imgMovieCardDoFavorite.setOnClickListener {
                movieListAdapterInterface.onMovieFavoritesAtPosition(adapterPosition)
            }
            view.imgMovieCardDoFavoriteDone.setOnClickListener {
                movieListAdapterInterface.onMovieFavoritesAtPosition(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        if (viewType == TYPE_FAVORITES) {
            val view = LayoutInflater.from(weakContext.get()).inflate(
                R.layout.layout_item_favorites, parent,
                false
            )

            return MovieFavoritesViewHolder(view, favoriteMovieAdapterInterface)
        }

        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.layout_item_movie, parent, false)
        return MovieItemViewHolder(view, movieListAdapterInterface)
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


    private var favoriteMovieAdapterInterface = object : FavoriteListAdapter.FavoriteListAdapterInterface {
        override fun onFavoriteMovieSelected(movie: Movie) {
            movieListAdapterInterface.onMovieSelected(
                SearchResultItem(
                    title = movie.title,
                    imdbID = movie.imdbID
                )
            )
        }
    }

    interface MovieListAdapterInterface {
        fun onMovieSelectedAtPosition(position: Int)
        fun onMovieSelected(item: SearchResultItem)
        fun onMovieFavoritesAtPosition(position: Int)
    }

}

data class MovieListDataItem(
    val type: Int,
    val item: Any
)

const val TYPE_FAVORITES = 1
const val TYPE_MOVIE = 2