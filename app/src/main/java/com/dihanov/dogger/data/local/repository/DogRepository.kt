package com.dihanov.dogger.data.local.repository

import com.dihanov.dogger.data.local.db.AppDb
import com.dihanov.dogger.data.local.db.entity.Dog
import com.dihanov.dogger.data.remote.ApiService
import com.dihanov.dogger.data.model.BaseResponse

class DogRepository(private val apiService: ApiService, private val db: AppDb) {
    suspend fun getDog(breed: String, limit: Int, shouldFetch: Boolean = false) = object : ResultLoader<List<Dog>, List<String>>() {
        override suspend fun processResult(item: List<String>): List<Dog> = item.map { element -> Dog(breed = breed, url = element) }

        override suspend fun writeToDb(toWrite: List<Dog>) = db.dogDao().writeDogs(toWrite)

        override fun shouldFetch(data: List<Dog>?): Boolean = shouldFetch

        override suspend fun loadFromCache(): List<Dog>? = db.dogDao().readAllDogs(breed)

        override suspend fun createCall(): BaseResponse<List<String>> = apiService.getDog(breed, limit)
    }.execute()
}