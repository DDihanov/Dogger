package com.dihanov.base_data.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dihanov.base_data.data.local.entity.Cat

@Dao
abstract class CatDao {
    open suspend fun writeCats(list: List<Cat>) {
        for (cat in list) {
            writeCat(cat)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun writeCat(cat: Cat)

    @Query("SELECT * from cat WHERE cat.breed == :breed")
    abstract suspend fun readAllCats(breed: String): List<Cat>
}