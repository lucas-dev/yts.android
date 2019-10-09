package com.alvefard.yify.presentation.screens.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvefard.yifymovies.R

class MovieViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val imageView: ImageView = root.findViewById(R.id.iv_cover)
    val tvTitle: TextView = root.findViewById(R.id.tv_title)
    val tvYear: TextView = root.findViewById(R.id.tv_year)
    val tvRuntime: TextView = root.findViewById(R.id.tv_runtime)
    val tvRating: TextView = root.findViewById(R.id.tv_rating)
}