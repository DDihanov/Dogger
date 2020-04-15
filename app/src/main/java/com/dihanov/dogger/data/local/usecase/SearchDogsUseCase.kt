package com.dihanov.dogger.data.local.usecase

import com.dihanov.dogger.data.local.db.AppDb
import com.dihanov.dogger.data.local.db.entity.Dog
import com.dihanov.dogger.data.local.mapper.DogMapper
import com.dihanov.dogger.data.remote.ApiService

class SearchDogsUseCase(
    private val breed: String,
    private val limit: Int,
    private val db: AppDb,
    private val shouldFetch: Boolean,
    private val dogMapper: DogMapper,
    private val service: ApiService
) : BaseUseCase<List<Dog>>() {
    override suspend fun execute() {
        val cached = db.dogDao().readAllDogs(breed)

        loading(cached)

        if (shouldFetch) {
            val response = ApiResponse.create(service.getDog2(breed, limit))

            when (response) {
                is ApiSuccessResponse -> {
                    val newList = dogMapper.mapFromEntity(Pair(breed, response.body.message))
                    db.dogDao().writeDogs(newList)
                    success(db.dogDao().readAllDogs(breed))
                }
                is ApiErrorResponse -> {
                    error(response.errorMessage, cached)
                }
                is ApiEmptyResponse -> {
                    error("", null)
                }
            }
        } else {
            success(cached)
        }
    }
}