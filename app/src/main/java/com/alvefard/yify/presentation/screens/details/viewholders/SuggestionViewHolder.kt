package com.alvefard.yify.presentation.screens.details.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvefard.yifymovies.R

class SuggestionViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val imageView: ImageView = root.findViewById(R.id.iv_suggestion)
    val tvTitle: TextView = root.findViewById(R.id.tv_suggestion_title)
    val tvYear: TextView = root.findViewById(R.id.tv_suggestions_year)
    val rbSuggestions: RatingBar = root.findViewById(R.id.rb_suggestion)
}
