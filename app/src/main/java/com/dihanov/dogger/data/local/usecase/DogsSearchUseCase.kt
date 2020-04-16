package com.dihanov.dogger.data.local.usecase

import com.dihanov.dogger.data.local.db.entity.Dog
import com.dihanov.dogger.data.local.mapper.DogMapper
import com.dihanov.dogger.data.local.repository.DogRepository
import com.dihanov.dogger.data.local.repository.Resource

class DogsSearchUseCase(private val dogRepository: DogRepository, private val dogMapper: DogMapper) : BaseUseCase<List<Dog>, DogsSearchUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Dog>> {
        val cached = dogRepository.getDogsFromCache(params.breed)

        val breed = params.breed
        val limit = params.limit
        val shouldFetch = params.shouldFetch

        return if (shouldFetch) {
            val response = ApiResponse.create(dogRepository.getDog2(breed, limit))

            when (response) {
                is ApiSuccessResponse -> {
                    val newList = dogMapper.mapFromEntity(Pair(breed, response.body.message))
                    dogRepository.writeDogs(newList)
                    success(dogRepository.getDogsFromCache(breed))
                }
                is ApiErrorResponse -> {
                    error(cached, response.errorMessage)
                }
                is ApiEmptyResponse -> {
                    error(cached, "Empty response")
                }
            }
        } else {
            success(cached)
        }
    }


    data class Params(val breed: String, val limit: Int, val shouldFetch: Boolean)
}