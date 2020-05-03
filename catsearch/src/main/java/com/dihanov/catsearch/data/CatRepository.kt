package com.dihanov.catsearch.data

import com.dihanov.catsearch.data.local.dao.CatDao
import com.dihanov.catsearch.data.local.entity.Cat
import com.dihanov.catsearch.data.remote.CatApiService

class CatRepository(
    private val apiService: CatApiService,
    private val catDao: CatDao
) {
    suspend fun getCats(breed: String, limit: Int) = apiService.getCats(breed, limit)

    suspend fun writeCats(dogs: List<Cat>) = catDao.writeCats(dogs)

    suspend fun getCatBreeds(limit: Int) = apiService.getCatBreeds(limit)

    suspend fun getCatsFromCache(breed: String) = catDao.readAllCats(breed)
}