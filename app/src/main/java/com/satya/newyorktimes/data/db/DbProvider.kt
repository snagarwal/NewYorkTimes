package com.satya.newyorktimes.data.db

import android.app.Application
import android.arch.persistence.room.Room

/**
 * Helper class to provide db and dao instance.
 */

object DbProvider{

    fun getDb(app:Application): NewsDB{
        return Room.databaseBuilder(app, NewsDB::class.java, "news.db")
            .fallbackToDestructiveMigration().build()
    }

    fun getNewsDao(db: NewsDB): NewsDao {
      return  db.newsDao()
    }

    fun getSectionDao(db: NewsDB): SectionDao {
        return  db.sectionDao()
    }
}