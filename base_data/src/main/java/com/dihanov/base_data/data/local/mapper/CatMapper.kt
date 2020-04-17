package com.dihanov.base_data.data.local.mapper

import com.dihanov.base_data.data.local.entity.Cat
import com.dihanov.base_data.data.remote.model.cat.Breeds
import com.dihanov.base_data.data.remote.model.cat.CatSearch

//first is breed, second is list of images
class CatMapper :
    Mapper<Pair<String, List<CatSearch>>, List<Cat>> {
    override fun mapFromEntity(type: Pair<String, List<CatSearch>>): List<Cat> {
        val breed = type.first
        val cats = type.second

        return cats.map { item -> Cat(breed = breed, url = item.url) }
    }

    override fun mapToEntity(type: List<Cat>): Pair<String, List<CatSearch>> {
        val breed = type.first().breed
        val images = type.map { item -> CatSearch(item.url, listOf<Breeds>()) }
        return Pair(breed, images)
    }
}