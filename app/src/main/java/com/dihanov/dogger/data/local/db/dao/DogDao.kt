package com.dihanov.dogger.data.local.db.dao

import androidx.room.*
import com.dihanov.dogger.data.local.db.entity.Dog

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