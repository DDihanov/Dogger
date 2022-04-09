package com.dihanov.dogcatcombined.domain

import com.dihanov.base_domain.BaseUseCase
import com.dihanov.base_domain.Resource
import com.dihanov.catsearch.data.CatRepository
import com.dihanov.dogcatcombined.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase.Params
import com.dihanov.dogsearch.data.DogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.min

class GetCombinedCatsAndDogsWithRandomBreedUseCase(
    private val dogRepository: DogRepository,
    private val catsRepository: CatRepository
) : BaseUseCase<List<String>, Params>() {
    companion object {
        const val MAX_CAT_BREEDS = 100
    }

    override suspend fun execute(params: Params): Resource<List<String>> {
        return try {
            coroutineScope {
                //fetch all breed lists
                val dogBreedsDeferred = async { dogRepository.getDogBreeds().breeds }
                val catBreedsDeferred = async { catsRepository.getCatBreeds(MAX_CAT_BREEDS) }

                val dogBreeds = dogBreedsDeferred.await()
                val catBreeds = catBreedsDeferred.await()

                //pick random spot in the array
                var startRandom = (0..dogBreeds.count()).random()
                var endRandom = min(dogBreeds.count(), startRandom + params.randomBreedsToFetch)

                val listOfDogs = dogBreeds.subList(startRandom, endRandom)

                //fetch # of images from the API for all the random breeds
                val listToAddDogs = mutableListOf<String>()
                for (dogBreed in listOfDogs) {
                    val fetchedBreed = coroutineScope {
                        dogRepository.getDog2(
                            dogBreed,
                            params.limitOfImagesPerBreed
                        )
                    }
                    listToAddDogs.addAll(fetchedBreed.message)
                }

                //do same thing for cats
                startRandom = (0..catBreeds.count()).random()
                endRandom = min(catBreeds.count(), startRandom + params.randomBreedsToFetch)
                val listOfCats = catBreeds.subList(startRandom, endRandom)

                val listToAddCats = mutableListOf<String>()
                for (catBreed in listOfCats) {
                    val fetchedBreed =
                        coroutineScope {
                            catsRepository.getCats(
                                catBreed.breed,
                                params.limitOfImagesPerBreed
                            )
                        }
                    listToAddCats.addAll(fetchedBreed.map { item -> item.url })
                }

                //combine both arrays into one list of images
                listToAddCats.addAll(listToAddDogs)
                listToAddCats.shuffle()

                success(listToAddCats)
            }
        } catch (ex: Exception) {
            error(listOf(), ex.message.toString())
        }
    }

    data class Params(val randomBreedsToFetch: Int, val limitOfImagesPerBreed: Int)
}