package com.dihanov.dogsearch.domain

import com.dihanov.base_domain.BaseUseCase
import com.dihanov.base_domain.Resource
import com.dihanov.dogsearch.data.DogRepository
import com.dihanov.dogsearch.data.local.entity.Dog
import com.dihanov.dogsearch.data.mapper.DogMapper

class DogsSearchUseCase(private val dogRepository: DogRepository, private val dogMapper: DogMapper) : BaseUseCase<List<Dog>, DogsSearchUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Dog>> {
        val cached = dogRepository.getDogsFromCache(params.breed)

        val breed = params.breed
        val limit = params.limit
        val shouldFetch = params.shouldFetch

        return if (shouldFetch) {
            try {
                val response = dogRepository.getDog2(breed, limit)
                val newList = dogMapper.mapFromEntity(Pair(breed, response.message))
                dogRepository.writeDogs(newList)
                success(dogRepository.getDogsFromCache(breed))
            } catch (ex: Exception) {
                error(cached, ex.message.toString())
            }
        } else {
            success(cached)
        }
    }


    data class Params(val breed: String, val limit: Int, val shouldFetch: Boolean)
}