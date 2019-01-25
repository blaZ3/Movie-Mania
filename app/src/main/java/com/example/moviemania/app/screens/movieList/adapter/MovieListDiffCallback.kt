package com.example.moviemania.app.screens.movieList.adapter

import androidx.recyclerview.widget.DiffUtil

class MovieListDiffCallback(private val oldItems: List<MovieListDataItem>, private val newItems: List<MovieListDataItem>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}