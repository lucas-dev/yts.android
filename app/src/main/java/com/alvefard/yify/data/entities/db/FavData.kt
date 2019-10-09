package com.alvefard.yify.data.entities.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alvefard.yify.data.Constants.FAV_TABLE
import com.alvefard.yify.data.entities.yify.movies.MovieItemSchema

@Entity(tableName = FAV_TABLE)
data class FavData(
        @NonNull @PrimaryKey val id: String,
         val movie: MovieItemSchema? = null)