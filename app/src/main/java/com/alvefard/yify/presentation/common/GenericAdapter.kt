package com.alvefard.yify.presentation.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


abstract class GenericAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<T> = mutableListOf()
    private val viewClickSubject = PublishSubject.create<T>()

    val viewClickObserable: Observable<T>
        get() = viewClickSubject.hide()

    abstract fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindData(holder: RecyclerView.ViewHolder, `val`: T)

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        onBindData(viewHolder, item)
        viewHolder.itemView.clicks()
                .map { item }
                .subscribe(viewClickSubject)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        viewClickSubject.onComplete()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    abstract fun updateData(items: MutableList<T>)

    fun getItems(): MutableList<T> {
        return items
    }

    fun getItem(position: Int): T {
        return items[position]
    }
}
