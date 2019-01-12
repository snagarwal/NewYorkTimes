package com.satya.newyorktimes.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity
data class Section(

    @Expose
    @PrimaryKey()
    val section: String
)