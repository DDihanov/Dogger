package com.dihanov.dogsearch.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dihanov.dogsearch.data.local.entity.Dog

@Dao
abstract class DogDao {
    open suspend fun writeDogs(list: List<Dog>) {
        for (dog in list) {
            writeDog(dog)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun writeDog(dog: Dog)

    @Query("SELECT * from dog WHERE dog.breed == :breed")
    abstract suspend fun readAllDogs(breed: String): List<Dog>
}