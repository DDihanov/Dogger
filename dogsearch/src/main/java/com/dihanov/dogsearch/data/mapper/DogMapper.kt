package com.dihanov.dogsearch.data.mapper

import com.dihanov.base_data.mapper.Mapper
import com.dihanov.dogsearch.data.local.entity.Dog

//first is breed, second is list of images
class DogMapper :
    Mapper<Pair<String, List<String>>, List<Dog>> {
    override fun mapFromEntity(type: Pair<String, List<String>>): List<Dog> {
        val breed = type.first
        val images = type.second
        return images.map { item -> Dog(breed = breed, url = item) }
    }

    override fun mapToEntity(type: List<Dog>): Pair<String, List<String>> {
        val images = type.map { item -> item.url }
        val breed = type.first().breed
        return Pair(breed, images)
    }
}