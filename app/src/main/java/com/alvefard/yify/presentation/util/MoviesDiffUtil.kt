package com.alvefard.yify.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.alvefard.yify.domain.model.Movie

class MoviesDiffUtil(private val oldList: MutableList<Movie>, private val newList: MutableList<Movie>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}