package com.alvefard.yify.data.db.converter

import androidx.room.TypeConverter
import com.alvefard.yify.data.entities.yify.movies.MovieItemSchema
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MovieSchemaListTypeConverter {
    @TypeConverter
    fun stringToMovieItemSchemaList(data: String): MutableList<MovieItemSchema> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<MutableList<MovieItemSchema>>() {
        }.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun movieItemSchemaListToString(list: MutableList<MovieItemSchema>): String {
        return Gson().toJson(list)
    }
}