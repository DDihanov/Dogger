package com.dihanov.dogsearch.data

import com.dihanov.dogsearch.data.local.dao.DogDao
import com.dihanov.dogsearch.data.local.entity.Dog
import com.dihanov.dogsearch.data.remote.DogApiService

class DogRepository(
    private val apiService: DogApiService,
    private val dogDao: DogDao
) {
    suspend fun getDog2(breed: String, limit: Int) = apiService.getDog2(breed, limit)

    suspend fun writeDogs(dogs: List<Dog>) {
        dogDao.writeDogs(dogs)
    }

    suspend fun getDogBreeds() = apiService.getBreeds()

    suspend fun getDogsFromCache(breed: String) = dogDao.readAllDogs(breed)
}