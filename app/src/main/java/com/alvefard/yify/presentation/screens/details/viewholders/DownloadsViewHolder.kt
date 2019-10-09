package com.alvefard.yify.presentation.screens.details.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alvefard.yifymovies.R

class DownloadsViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val tvTitle: TextView = root.findViewById(R.id.tv_download_title)
    val tvFormat: TextView = root.findViewById(R.id.tv_download_format)
    val tvSize: TextView = root.findViewById(R.id.tv_download_size)
    val tvSeeds: TextView = root.findViewById(R.id.tv_download_seeds)
    val tvPeers: TextView = root.findViewById(R.id.tv_download_peers)
}