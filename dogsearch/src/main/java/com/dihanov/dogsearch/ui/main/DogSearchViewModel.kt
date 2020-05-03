package com.dihanov.dogsearch.ui.main

import androidx.lifecycle.*
import com.dihanov.base_domain.Resource
import com.dihanov.dogsearch.domain.DogsSearchUseCase
import com.dihanov.dogsearch.domain.GetCachedDogsUseCase
import com.dihanov.dogsearch.ui.uimodel.GetDog
import com.dihanov.util.Event
import kotlinx.coroutines.Dispatchers

class DogSearchViewModel(private val getCachedDogsUseCase: GetCachedDogsUseCase, private val dogsSearchUseCase: DogsSearchUseCase) : ViewModel() {

    private val _dogImages = MutableLiveData<GetDog>()
    val dogImages = _dogImages.switchMap { model ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Event(Resource.loading(getCachedDogsUseCase.execute(GetCachedDogsUseCase.Params(model.breed)).data)))
            emit(Event(dogsSearchUseCase.execute(DogsSearchUseCase.Params(model.breed, model.limit, true))))
        }
    }

    fun searchForDogs(getDog: GetDog) {
        _dogImages.value = getDog
    }
}
