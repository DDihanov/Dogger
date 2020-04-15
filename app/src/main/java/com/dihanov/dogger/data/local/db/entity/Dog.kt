package com.dihanov.dogger.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "breed") val breed: String = "",
    @ColumnInfo(name = "url") val url: String = ""
)