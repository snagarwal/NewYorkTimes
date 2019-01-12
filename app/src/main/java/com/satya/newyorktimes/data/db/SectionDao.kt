package com.satya.newyorktimes.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.satya.newyorktimes.data.model.Section

/**
 * Interface for database access for User related operations.
 */
@Dao
interface SectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(section: List<Section>)

    @Query("SELECT * FROM section")
    fun getAll(): LiveData<List<Section>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSection(section: Section)

}