package com.dihanov.dogcatcombined.withoutusecase

import androidx.lifecycle.*
import com.dihanov.base_domain.Resource
import com.dihanov.dogsearch.data.DogRepository
import com.dihanov.dogsearch.data.mapper.DogMapper
import com.dihanov.dogsearch.ui.uimodel.GetDog
import kotlinx.coroutines.Dispatchers

//compare to DogSearchUseCase in dogsearch module
class DogSearchViewModelWithoutUseCase(private val dogRepository: DogRepository, private val dogMapper: DogMapper) : ViewModel() {

    private val _dogImages = MutableLiveData<GetDog>()
    val dogImages = _dogImages.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            val cached = dogRepository.getDogsFromCache(model.breed)

            emit(cached)

            val breed = model.breed
            val limit = model.limit
            val shouldFetch = true

            val value = if (shouldFetch) {
                try {
                    val response = dogRepository.getDog2(breed, limit)
                    val newList = dogMapper.mapFromEntity(Pair(breed, response.message))
                    dogRepository.writeDogs(newList)
                    Resource.success(dogRepository.getDogsFromCache(breed))
                } catch (ex: Exception) {
                    Resource.error(ex.message.toString(), cached)
                }
            } else {
                Resource.success(cached)
            }

            emit(value)
        }
    }

    fun searchForDogs(getDog: GetDog) {
        _dogImages.value = getDog
    }
}
