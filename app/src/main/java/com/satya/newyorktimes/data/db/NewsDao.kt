package com.satya.newyorktimes.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.satya.newyorktimes.data.model.News

/**
 * Interface for database access for User related operations.
 */
@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: List<News>)

    @Query("SELECT * FROM news WHERE section = :section ORDER BY published_date DESC ")
    fun findBySection(section: String): LiveData<List<News>>

    @Query("SELECT * FROM news ORDER BY published_date DESC ")
    fun getAll(): LiveData<List<News>>

    @Query("SELECT * FROM news WHERE title= :title")
    fun findByTitle(title: String): LiveData<News>

    @Query("DELETE  FROM news")
    fun clear()

}