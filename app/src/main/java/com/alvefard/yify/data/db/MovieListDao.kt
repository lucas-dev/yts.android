package com.alvefard.yify.data.db

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alvefard.yify.data.Constants.COLUMN_LATEST
import com.alvefard.yify.data.Constants.COLUMN_POPULAR
import com.alvefard.yify.data.Constants.COLUMN_RATED
import com.alvefard.yify.data.Constants.TABLE_NAME
import com.alvefard.yify.data.entities.db.MovieListData
import io.reactivex.Single

@Dao
interface MovieListDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE type ='$COLUMN_LATEST'")
    fun fetchLatestMovies(): Single<MovieListData>

    @Query("SELECT * FROM $TABLE_NAME WHERE type ='$COLUMN_POPULAR'")
    fun fetchPopularMovies():Single<MovieListData>

    @Query("SELECT * FROM $TABLE_NAME WHERE type ='$COLUMN_RATED'")
    fun fetchTopRatedMovies():Single<MovieListData>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTopRatedMovies(movieListData: MovieListData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLatestMovies(movieListData: MovieListData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePopularMovies(movieListData: MovieListData)
}