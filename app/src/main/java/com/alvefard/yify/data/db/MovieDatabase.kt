package com.alvefard.yify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alvefard.yify.data.db.converter.FilterModelConverter
import com.alvefard.yify.data.db.converter.MovieSchemaListTypeConverter
import com.alvefard.yify.data.db.converter.MovieSchemaTypeConverter
import com.alvefard.yify.data.entities.db.FavData
import com.alvefard.yify.data.entities.db.MovieListData

@Database(entities = [MovieListData::class, FavData::class], version = 1,  exportSchema = false)
@TypeConverters(MovieSchemaListTypeConverter::class, FilterModelConverter::class, MovieSchemaTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun moviesListDao(): MovieListDao
    abstract fun favsDao(): FavsDao
}