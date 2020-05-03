package com.dihanov.catsearch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class CatDao {
    open suspend fun writeCats(list: List<com.dihanov.catsearch.data.local.entity.Cat>) {
        for (cat in list) {
            writeCat(cat)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun writeCat(cat: com.dihanov.catsearch.data.local.entity.Cat)

    @Query("SELECT * from cat WHERE cat.breed == :breed")
    abstract suspend fun readAllCats(breed: String): List<com.dihanov.catsearch.data.local.entity.Cat>
}