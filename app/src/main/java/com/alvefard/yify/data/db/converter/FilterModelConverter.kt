package com.alvefard.yify.data.db.converter

import androidx.room.TypeConverter
import com.alvefard.yify.data.entities.FilterData
import com.google.gson.Gson

class FilterModelConverter {
    @TypeConverter
    fun fromString(filterJson:String): FilterData? {
        if (filterJson == null) return null
        return Gson().fromJson(filterJson, FilterData::class.java)
    }
    @TypeConverter
    fun toString(filterModel:FilterData): String? {
        if (filterModel == null) return null
        return Gson().toJson(filterModel)
    }
}