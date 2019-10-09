package com.alvefard.yify.data.db.converter

import androidx.room.TypeConverter
import com.alvefard.yify.data.entities.yify.movies.MovieItemSchema
import com.google.gson.Gson

class MovieSchemaTypeConverter {
    @TypeConverter
    fun fromString(movieItemJson: String): MovieItemSchema? {
        if (movieItemJson == null) return null
        return Gson().fromJson(movieItemJson, MovieItemSchema::class.java)
    }

    @TypeConverter
    fun toString(movieItemSchema: MovieItemSchema): String? {
        if (movieItemSchema == null) return null
        return Gson().toJson(movieItemSchema)
    }
}