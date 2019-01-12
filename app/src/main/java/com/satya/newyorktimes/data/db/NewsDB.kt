package com.satya.newyorktimes.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.satya.newyorktimes.data.model.News
import com.satya.newyorktimes.data.model.Section

/**
 * Room Database class
 */
@Database(entities = [News::class, Section::class], version = 1)
@TypeConverters(NewsTypeConverters::class)
abstract class NewsDB : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun sectionDao(): SectionDao

}