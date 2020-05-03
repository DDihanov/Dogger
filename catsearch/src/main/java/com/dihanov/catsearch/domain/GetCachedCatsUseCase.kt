package com.dihanov.catsearch.domain

import com.dihanov.base_domain.BaseUseCase
import com.dihanov.base_domain.Resource
import com.dihanov.catsearch.data.CatRepository
import com.dihanov.catsearch.data.local.entity.Cat

class GetCachedCatsUseCase(private val repository: CatRepository) : BaseUseCase<List<Cat>, GetCachedCatsUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Cat>> = Resource.success(repository.getCatsFromCache(params.breed))

    data class Params(val breed: String)
}