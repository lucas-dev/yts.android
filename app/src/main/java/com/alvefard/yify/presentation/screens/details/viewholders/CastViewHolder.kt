package com.alvefard.yify.presentation.screens.details.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvefard.yifymovies.R

class CastViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val imageView: ImageView = root.findViewById(R.id.iv_actor)
    val tvActor: TextView = root.findViewById(R.id.tv_actor)
    val tvCharacter: TextView = root.findViewById(R.id.tv_character)
}