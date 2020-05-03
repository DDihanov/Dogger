package com.dihanov.dogcatcombined.withoutusecase

import androidx.lifecycle.*
import com.dihanov.base_domain.Resource
import com.dihanov.catsearch.data.CatRepository
import com.dihanov.dogcatcombined.domain.GetCombinedCatsAndDogsWithRandomBreedUseCase
import com.dihanov.dogcatcombined.ui.combined.uimodel.CombinedDogCat
import com.dihanov.dogsearch.data.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.min

//compare to GetCombinedCatsAndDogsWithRandomBreedUseCase in dogcatcombined.domain module
class CombinedViewModelWithoutUseCase(
    private val dogRepository: DogRepository,
    private val catsRepository: CatRepository
) : ViewModel() {
    private val _combinedDogsCats = MutableLiveData<CombinedDogCat>()
    val combinedDogsCats = _combinedDogsCats.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                val params = model
                val toReturn =
                    coroutineScope {
                        //fetch all breed lists
                        val dogBreedsDeferred = async { dogRepository.getDogBreeds().breeds }
                        val catBreedsDeferred = async { catsRepository.getCatBreeds(
                            GetCombinedCatsAndDogsWithRandomBreedUseCase.MAX_CAT_BREEDS
                        ) }

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
                                    params.numberOfImagesPerBreed
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
                                        params.numberOfImagesPerBreed
                                    )
                                }
                            listToAddCats.addAll(fetchedBreed.map { item -> item.url })
                        }

                        //combine both arrays into one list of images
                        listToAddCats.addAll(listToAddDogs)
                        listToAddCats.shuffle()
                    }

                emit(Resource.success(toReturn))
            } catch (ex: Exception) {
                Resource.error(ex.message.toString(), null)
            }
        }
    }

    fun getCombinedDogsAndCats(model: CombinedDogCat) {
        _combinedDogsCats.value = model
    }
}