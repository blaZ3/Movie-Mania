package com.example.moviemania.app.screens.movieList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemania.R
import com.example.moviemania.app.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_fav_movie.view.*

class FavoriteListAdapter(
    var items: List<Movie>,
    private val context: Context,
    adapterInterface: FavoriteListAdapterInterface
) : RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    init {
        favoriteListAdapterInterface = adapterInterface
    }

    class FavoriteListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(item: Movie) {
            view.txtFavoriteMovieName.text = item.title
            Picasso.get().load(item.poster).into(view.imgFavoriteMoviePoster)

            view.tag = item
            view.setOnClickListener {
                favoriteListAdapterInterface?.onFavoriteMovieSelected(it.tag as Movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.layout_item_fav_movie, parent,
            false
        )
        return FavoriteListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    companion object {
        private var favoriteListAdapterInterface: FavoriteListAdapterInterface? = null
    }

    interface FavoriteListAdapterInterface {
        fun onFavoriteMovieSelected(movie: Movie)
    }


}