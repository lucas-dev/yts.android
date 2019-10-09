package com.alvefard.yify.presentation.di

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alvefard.yify.data.Constants.COLUMN_LATEST
import com.alvefard.yify.data.Constants.COLUMN_POPULAR
import com.alvefard.yify.data.Constants.COLUMN_RATED
import com.alvefard.yify.data.Constants.DATABASE_NAME
import com.alvefard.yify.data.Constants.TABLE_NAME
import com.alvefard.yify.data.db.MovieDatabase
import com.alvefard.yify.presentation.screens.landing.di.LandingScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    @LandingScope
    fun provideRoomDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, DATABASE_NAME).addCallback(object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.insert(TABLE_NAME, OnConflictStrategy.IGNORE, getEmptyMovie(COLUMN_LATEST))
                db.insert(TABLE_NAME, OnConflictStrategy.IGNORE, getEmptyMovie(COLUMN_POPULAR))
                db.insert(TABLE_NAME, OnConflictStrategy.IGNORE, getEmptyMovie(COLUMN_RATED))
            }
        }).build()
    }

    private fun getEmptyMovie(columnLatest: String): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("type", columnLatest)
        contentValues.put("currentPage", 0)
        contentValues.put("movies", "")
        contentValues.put("filters", "")
        return contentValues
    }
}