package com.dihanov.catsearch.domain

import com.dihanov.base_data.data.local.entity.Cat
import com.dihanov.base_data.data.local.mapper.CatMapper
import com.dihanov.base_domain.domain.BaseUseCase
import com.dihanov.base_domain.domain.Resource
import com.dihanov.catsearch.data.local.repository.CatRepository


class CatsSearchUseCase(private val catRepository: CatRepository, private val catMapper: CatMapper) : BaseUseCase<List<Cat>, CatsSearchUseCase.Params>() {
    override suspend fun execute(params: Params): Resource<List<Cat>> {
        val cached = catRepository.getCatsFromCache(params.breed)

        val breed = params.breed
        val limit = params.limit
        val shouldFetch = params.shouldFetch

        return if (shouldFetch) {
            try {
                val response = catRepository.getCats(breed, limit)
                val newList = catMapper.mapFromEntity(Pair(breed, response))
                catRepository.writeCats(newList)
                success(catRepository.getCatsFromCache(breed))
            } catch (ex: Exception) {
                error(cached, ex.message.toString())
            }
        } else {
            success(cached)
        }
    }


    data class Params(val breed: String, val limit: Int, val shouldFetch: Boolean)
}