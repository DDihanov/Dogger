package com.dihanov.dogsearch.domain

import com.dihanov.base_domain.BaseUseCase
import com.dihanov.base_domain.Resource
import com.dihanov.dogsearch.data.DogRepository
import com.dihanov.dogsearch.data.local.entity.Dog

class GetCachedDogsUseCase(private val repository: DogRepository) : BaseUseCase<List<Dog>, GetCachedDogsUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Dog>> = Resource.success(repository.getDogsFromCache(params.breed))

    data class Params(val breed: String)
}