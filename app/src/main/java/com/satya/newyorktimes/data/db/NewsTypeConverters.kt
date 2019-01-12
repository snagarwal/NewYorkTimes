package com.satya.newyorktimes.data.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satya.newyorktimes.data.model.Multimedia
import java.util.ArrayList

/**
 * class to define type converters for room database
 */
class NewsTypeConverters {

    @TypeConverter
    fun stringToMultimedia(data: String?): List<Multimedia>? {

        val listType = object : TypeToken<ArrayList<Multimedia>>() {

        }.type
        return Gson().fromJson<List<Multimedia>>(data, listType)
    }

    @TypeConverter
    fun multimediaToString(multimedia: List<Multimedia>?): String? {
        return Gson().toJson(multimedia)
    }

    @TypeConverter
    fun stringToObject(data: String?): List<String>? {
        if (data != null) {
            val listType = object : TypeToken<List<String>>() {

            }.type
            return Gson().fromJson<List<String>>(data, listType)
        }
        return null
    }

    @TypeConverter
    fun objectToString(stringList: List<String>?): String? {
        if (stringList != null) {
            val gson = Gson()
            return gson.toJson(stringList)
        }
        return null
    }

}