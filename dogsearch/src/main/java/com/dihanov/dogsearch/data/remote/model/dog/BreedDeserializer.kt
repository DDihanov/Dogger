package com.dihanov.dogsearch.data.remote.model.dog

import com.squareup.moshi.FromJson

class BreedDeserializer {
    @FromJson
    fun fromJson(breedJson: Any): DogBreedSearch {
        return try {
            val get = (breedJson as Map<String, Map<String, List<String>>>)["message"]

            val toReturn = get!!.keys.toList()

            DogBreedSearch(toReturn)
        } catch (ex: Exception) {
            DogBreedSearch(listOf())
        }
    }
}