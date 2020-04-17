package com.dihanov.catsearch.domain

import com.dihanov.base_data.data.local.entity.Cat
import com.dihanov.base_domain.domain.BaseUseCase
import com.dihanov.base_domain.domain.Resource
import com.dihanov.catsearch.data.local.repository.CatRepository

class GetCachedCatsUseCase(private val repository: CatRepository) : BaseUseCase<List<Cat>, GetCachedCatsUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Cat>> = Resource.success(repository.getCatsFromCache(params.breed))

    data class Params(val breed: String)
}